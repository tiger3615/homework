package xyz.trieye.assignment.deal.biz.portfolio;

import org.springframework.stereotype.Service;
import xyz.trieye.assignment.deal.biz.asset.AssetDTO;
import xyz.trieye.assignment.deal.biz.asset.AssetDao;
import xyz.trieye.assignment.deal.biz.asset.SecurityTypes;
import xyz.trieye.assignment.deal.biz.option.OptionDTO;
import xyz.trieye.assignment.deal.biz.option.OptionDao;
import xyz.trieye.assignment.deal.biz.option.OptionTypes;
import xyz.trieye.assignment.deal.biz.stock.StockDTO;
import xyz.trieye.assignment.deal.biz.stock.StockDao;
import xyz.trieye.assignment.deal.common.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final AssetDao assetDao;
    private final StockDao stockDao;
    private final OptionDao optionDao;

    public PortfolioService(AssetDao assetDao, StockDao stockDao, OptionDao optionDao) {
        this.assetDao = assetDao;
        this.stockDao = stockDao;
        this.optionDao = optionDao;
    }

    public PortfolioSummaryVO getPortfolioByUserId(int userId) {
        List<AssetDTO> assetDTOS = assetDao.queryByUserId(userId);

        List<Integer> stockTickerIds = assetDTOS.stream()
                .filter(assetDTO -> SecurityTypes.STOCK.equals(assetDTO.getSecurityType()))
                .map(AssetDTO::getSecurityId)
                .collect(Collectors.toList());
        Map<Integer, StockDTO> stockMap = stockDao.queryStocksByTickerIds(stockTickerIds);

        List<Integer> optionTickerIds = assetDTOS.stream()
                .filter(assetDTO -> SecurityTypes.OPTION.equals(assetDTO.getSecurityType()))
                .map(AssetDTO::getSecurityId)
                .collect(Collectors.toList());
        Map<Integer, OptionDTO> optionMap = optionDao.queryOptionsByTickerIds(optionTickerIds);
        List<PortfolioVO> portfolios = assetDTOS.stream().map(assetDTO -> {
            if (SecurityTypes.STOCK.equals(assetDTO.getSecurityType())) {
                StockDTO stockDTO = stockMap.get(assetDTO.getSecurityId());
                return PortfolioVO.builder().ticker(stockDTO.getTicker())
                        .price(stockDTO.getPrice())
                        .position(assetDTO.getPosition())
                        .value(stockDTO.getPrice().multiply(assetDTO.getPosition()))
                        .build();
            } else {
                OptionDTO optionDTO = optionMap.get(assetDTO.getSecurityId());
                BigDecimal optionPrice = calculateOptionPrice(optionDTO, stockMap);
                return PortfolioVO.builder().ticker(optionDTO.getTicker())
                        .price(optionPrice)
                        .position(assetDTO.getPosition())
                        .value(optionPrice.multiply(assetDTO.getPosition()))
                        .build();
            }
        }).collect(Collectors.toList());
        Optional<BigDecimal> gross = portfolios.stream().map(PortfolioVO::getValue).reduce((BigDecimal::add));
        return PortfolioSummaryVO.builder().portfolioVOList(portfolios).gross(gross.orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)).build();
    }

    private BigDecimal calculateOptionPrice(OptionDTO optionDTO, Map<Integer, StockDTO> stockMap) {
        StockDTO stockDTO = stockMap.get(optionDTO.getStockId());
        BigDecimal stockCurrentPrice = stockDTO.getPrice();
        BigDecimal optionStrikePrice = optionDTO.getStrike();
        int years = optionDTO.getMaturity();
        BigDecimal riskFreeInterestRate = new BigDecimal("0.02");
        BigDecimal deviation = stockDTO.getAnnualDeviation();
        if (OptionTypes.Call.equals(optionDTO.getType())) {
            return Tools.calculateCallOptionPrice(stockCurrentPrice, optionStrikePrice, riskFreeInterestRate, deviation, years).setScale(2, RoundingMode.HALF_UP);
        } else {
            return Tools.calculatePutOptionPrice(stockCurrentPrice, optionStrikePrice, riskFreeInterestRate, deviation, years).setScale(2, RoundingMode.HALF_UP);
        }
    }


}

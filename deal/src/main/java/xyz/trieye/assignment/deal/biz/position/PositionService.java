package xyz.trieye.assignment.deal.biz.position;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.trieye.assignment.deal.biz.asset.AssetDTO;
import xyz.trieye.assignment.deal.biz.asset.AssetDao;
import xyz.trieye.assignment.deal.biz.asset.SecurityTypes;
import xyz.trieye.assignment.deal.biz.position.vo.CommandVO;
import xyz.trieye.assignment.deal.biz.position.vo.PositionVO;
import xyz.trieye.assignment.deal.biz.option.OptionDTO;
import xyz.trieye.assignment.deal.biz.option.OptionDao;
import xyz.trieye.assignment.deal.biz.stock.StockDTO;
import xyz.trieye.assignment.deal.biz.stock.StockDao;
import xyz.trieye.assignment.deal.biz.user.UserDao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final UserDao userDao;
    private final StockDao stockDao;
    private final AssetDao assetDao;
    private final OptionDao optionDao;

    public PositionService(UserDao userDao, StockDao stockDao, AssetDao assetDao, OptionDao optionDao) {
        this.userDao = userDao;
        this.stockDao = stockDao;
        this.assetDao = assetDao;
        this.optionDao = optionDao;
    }

    public String validateBiz(CommandVO commandVO) {
        if (userDao.queryUserById(commandVO.getUserId()) == null) {
            return "The userId does not exist!";
        }
        for (PositionVO position : commandVO.getPositions()) {
            String ticker = position.getTicker();
            if (SecurityTypes.STOCK.equals(position.getSecurityType())) {
                if (stockDao.queryStockByTicker(ticker) == null) {
                    return String.format("Ticker %s does not exist", ticker);
                }
            }
        }
        return "OK";
    }

    @Transactional
    public void updatePositions(CommandVO commandVO) {
        assetDao.removeOldByUserId(commandVO.getUserId());

        List<String> stockTickers = commandVO.getPositions().stream()
                .filter(positionVO -> SecurityTypes.STOCK.equals(positionVO.getSecurityType()))
                .map(PositionVO::getTicker)
                .collect(Collectors.toList());
        Map<String, StockDTO> stockMap = stockDao.queryStocksByTickers(stockTickers);

        List<String> optionTickers = commandVO.getPositions().stream()
                .filter(positionVO -> SecurityTypes.OPTION.equals(positionVO.getSecurityType()))
                .map(PositionVO::getTicker)
                .collect(Collectors.toList());
        Map<String, OptionDTO> optionMap = optionDao.queryOptionsByTickers(optionTickers);

        List<AssetDTO> newAssets = commandVO.getPositions().stream().map(positionVO -> {
            if (SecurityTypes.STOCK.equals(positionVO.getSecurityType())) {
                StockDTO stockDTO = stockMap.get(positionVO.getTicker());
                return AssetDTO.builder()
                        .userId(commandVO.getUserId())
                        .securityType(SecurityTypes.STOCK)
                        .securityId(stockDTO.getId())
                        .position(positionVO.getPosition())
                        .build();
            } else {
                OptionDTO optionDTO = optionMap.get(positionVO.getTicker());
                return AssetDTO.builder()
                        .userId(commandVO.getUserId())
                        .securityType(SecurityTypes.OPTION)
                        .securityId(optionDTO.getId())
                        .position(positionVO.getPosition())
                        .build();
            }
        }).collect(Collectors.toList());
        assetDao.insertAssets(newAssets);
    }
}

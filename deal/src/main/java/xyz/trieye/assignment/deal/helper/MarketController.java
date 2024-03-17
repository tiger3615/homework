package xyz.trieye.assignment.deal.helper;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.trieye.assignment.deal.biz.stock.StockDTO;
import xyz.trieye.assignment.deal.biz.stock.StockDao;
import xyz.trieye.assignment.deal.common.Tools;

import java.math.BigDecimal;

@RestController
@RequestMapping("/market")
public class MarketController {

    // Should generate service for calling stockDao.
    private final StockDao stockDao;

    public MarketController(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @PutMapping("/updateStockPrice/{ticker}")
    public String updatePrice(@PathVariable String ticker){
        StockDTO stockDTO = stockDao.queryStockByTicker(ticker);
        BigDecimal newPrice = PriceSimulator.nextPrice(stockDTO.getPrice(), 86400L);
        stockDTO.setPrice(newPrice);
        stockDao.updatePrice(stockDTO);
        return stockDTO.getTicker() + " change to " + Tools.priceFormat.format(newPrice);
    }

}

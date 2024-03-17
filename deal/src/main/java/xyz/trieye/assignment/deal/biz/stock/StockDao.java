package xyz.trieye.assignment.deal.biz.stock;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockDao {
    @Select("select id, ticker, price, annual_deviation annualDeviation from stock_t where ticker=#{ticker}")
    StockDTO queryStockByTicker(String ticker);

    @MapKey("ticker")
    @Select("<script>\n" +
            "\tselect id, ticker, price, annual_deviation annualDeviation from stock_t where ticker IN \n" +
            "\t<foreach item='item' collection='list' open ='(' separator=',' close=')' >\n" +
            "\t\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    Map<String, StockDTO> queryStocksByTickers(List<String> tickers);

    @MapKey("id")
    @Select("<script>\n" +
            "\tselect id, ticker, price, annual_deviation annualDeviation from stock_t where id IN \n" +
            "\t<foreach item='item' collection='list' open ='(' separator=',' close=')' >\n" +
            "\t\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    Map<Integer, StockDTO> queryStocksByTickerIds(List<Integer> tickerIds);

    @Update("update stock_t set price = #{price} where id=#{id}")
    int updatePrice(StockDTO stockDTO);
}

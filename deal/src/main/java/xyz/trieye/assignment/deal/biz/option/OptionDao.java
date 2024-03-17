package xyz.trieye.assignment.deal.biz.option;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OptionDao {
    @MapKey("ticker")
    @Select("<script>\n" +
            "\tselect id, ticker, type, stock_id stockId, strike, maturity\n" +
            "\tfrom option_t where ticker IN \n" +
            "\t<foreach item='item' collection='tickers' open ='(' separator=',' close=')' >\n" +
            "\t\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    Map<String, OptionDTO> queryOptionsByTickers(@Param("tickers") List<String> tickers);


    @MapKey("id")
    @Select("<script>\n" +
            "\tselect id, ticker, type, stock_id stockId, strike, maturity\n" +
            "\tfrom option_t where id IN \n" +
            "\t<foreach item='item' collection='tickers' open ='(' separator=',' close=')' >\n" +
            "\t\t#{item}\n" +
            "\t</foreach>\n" +
            "</script>")
    Map<Integer, OptionDTO> queryOptionsByTickerIds(@Param("tickers") List<Integer> tickerIds);
}

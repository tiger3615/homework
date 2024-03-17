package xyz.trieye.assignment.deal.integrate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import xyz.trieye.assignment.deal.biz.stock.StockDTO;
import xyz.trieye.assignment.deal.biz.stock.StockDao;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MarketTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StockDao stockDao;

    @Test
    public void price_should_update_to_DB() throws Exception {
        MvcResult mvcResult = mvc.perform(put("/market/updateStockPrice/APPL"))
                .andExpect(status().isOk())
                .andReturn();
        String returnMsg = mvcResult.getResponse().getContentAsString();
        StockDTO stockDTO = stockDao.queryStockByTicker("APPL");
        String newPrice = returnMsg.split("\\s")[3];
        Assertions.assertThat(stockDTO).extracting("price").isEqualTo(new BigDecimal(newPrice));
    }
}

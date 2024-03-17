package xyz.trieye.assignment.deal.integrate;

import com.alibaba.fastjson.JSON;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import xyz.trieye.assignment.deal.biz.asset.SecurityTypes;
import xyz.trieye.assignment.deal.biz.portfolio.PortfolioSummaryVO;
import xyz.trieye.assignment.deal.biz.portfolio.PortfolioVO;
import xyz.trieye.assignment.deal.biz.position.vo.CommandVO;
import xyz.trieye.assignment.deal.biz.position.vo.PositionVO;
import xyz.trieye.assignment.deal.common.Tools;
import xyz.trieye.assignment.deal.helper.PriceSimulator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PortfolioTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void when_update_price_then_should_calculate_portfolio() throws Exception {
        CommandVO commandVO1 = CommandVO.builder().userId(1).build();
        PositionVO position1 = PositionVO.builder().ticker("APPL").securityType(SecurityTypes.STOCK).position(new BigDecimal(100)).build();
        PositionVO position11 = PositionVO.builder().ticker("AAPL-OCT-2020-110-C").securityType(SecurityTypes.OPTION).position(new BigDecimal(-2000)).build();
        PositionVO position12 = PositionVO.builder().ticker("AAPL-OCT-2020-110-P").securityType(SecurityTypes.OPTION).position(new BigDecimal(2000)).build();
        PositionVO position2 = PositionVO.builder().ticker("TELSA").securityType(SecurityTypes.STOCK).position(new BigDecimal(-50)).build();
        PositionVO position21 = PositionVO.builder().ticker("TELSA-NOV-2020-400-C").securityType(SecurityTypes.OPTION).position(new BigDecimal(1000)).build();
        PositionVO position22 = PositionVO.builder().ticker("TELSA-DEC-2020-400-P").securityType(SecurityTypes.OPTION).position(new BigDecimal(-1000)).build();
        commandVO1.setPositions(Arrays.asList(position1, position11, position12, position2, position21, position22));
        mvc.perform(post("/position/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(commandVO1)))
                .andExpect(content().string("OK"));

        try (MockedStatic<PriceSimulator> mockedStatic = Mockito.mockStatic(PriceSimulator.class)) {
            mockedStatic.when(PriceSimulator::nextRandom).thenReturn(new BigDecimal(0.6));
            mockedStatic.when(() -> PriceSimulator.nextPrice(new BigDecimal(100), 86400L)).thenCallRealMethod();
            mockedStatic.when(() -> PriceSimulator.nextPrice(new BigDecimal(200), 86400L)).thenCallRealMethod();
            MvcResult mvcResult = mvc.perform(put("/market/updateStockPrice/APPL"))
                    .andExpect(status().isOk())
                    .andReturn();
            String returnMsg = mvcResult.getResponse().getContentAsString();
            System.out.println(returnMsg);
            MvcResult mvcResult2 = mvc.perform(put("/market/updateStockPrice/TELSA"))
                    .andExpect(status().isOk())
                    .andReturn();
            String returnMsg2 = mvcResult2.getResponse().getContentAsString();
            System.out.println(returnMsg2);
            MvcResult mvcResult3 = mvc.perform(get("/portfolio/userId/1"))
                    .andExpect(status().isOk())
                    .andReturn();
            PortfolioSummaryVO portfolioSummaryVO = JSON.parseObject(mvcResult3.getResponse().getContentAsString(), PortfolioSummaryVO.class);
            Assertions.assertThat(portfolioSummaryVO).hasFieldOrPropertyWithValue("gross", new BigDecimal("3729.50"))
                    .extracting("portfolioVOList")
                    .asList()
                    .hasSize(6)
                    .extracting("ticker", "price", "position", "value")
                    .containsExactly(Tuple.tuple("APPL", new BigDecimal("104.4"), new BigDecimal("100"), new BigDecimal("10440.0")),
                            Tuple.tuple("AAPL-OCT-2020-110-C", new BigDecimal("16.17"), new BigDecimal("-2000"), new BigDecimal("-32340.00")),
                            Tuple.tuple("AAPL-OCT-2020-110-P", new BigDecimal("5.94"), new BigDecimal("2000"), new BigDecimal("11880.00")),
                            Tuple.tuple("TELSA", new BigDecimal("208.81"), new BigDecimal("-50"), new BigDecimal("-10440.50")),
                            Tuple.tuple("TELSA-NOV-2020-400-C", new BigDecimal("37.48"), new BigDecimal("1000"), new BigDecimal("37480.00")),
                            Tuple.tuple("TELSA-DEC-2020-400-P", new BigDecimal("13.29"), new BigDecimal("-1000"), new BigDecimal("-13290.00")));
        }
    }

    @Test
    public void demo() throws Exception {
        // set positions
        CommandVO commandVO1 = CommandVO.builder().userId(1).build();
        PositionVO position1 = PositionVO.builder().ticker("APPL").securityType(SecurityTypes.STOCK).position(new BigDecimal(100)).build();
        PositionVO position11 = PositionVO.builder().ticker("AAPL-OCT-2020-110-C").securityType(SecurityTypes.OPTION).position(new BigDecimal(-2000)).build();
        PositionVO position12 = PositionVO.builder().ticker("AAPL-OCT-2020-110-P").securityType(SecurityTypes.OPTION).position(new BigDecimal(2000)).build();
        PositionVO position2 = PositionVO.builder().ticker("TELSA").securityType(SecurityTypes.STOCK).position(new BigDecimal(-50)).build();
        PositionVO position21 = PositionVO.builder().ticker("TELSA-NOV-2020-400-C").securityType(SecurityTypes.OPTION).position(new BigDecimal(1000)).build();
        PositionVO position22 = PositionVO.builder().ticker("TELSA-DEC-2020-400-P").securityType(SecurityTypes.OPTION).position(new BigDecimal(-1000)).build();
        commandVO1.setPositions(Arrays.asList(position1, position11, position12, position2, position21, position22));
        mvc.perform(post("/position/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(commandVO1)))
                .andExpect(content().string("OK"));
        try (MockedStatic<PriceSimulator> mockedStatic = Mockito.mockStatic(PriceSimulator.class)) {
            mockedStatic.when(PriceSimulator::nextRandom).thenReturn(new BigDecimal(0.6));
            mockedStatic.when(() -> PriceSimulator.nextPrice(Mockito.any(BigDecimal.class), Mockito.anyLong())).thenCallRealMethod();
            for (int i = 0; i < 5; i++) {
                System.out.println("## " + (i + 1) + " Market Data Update");
                MvcResult mvcResult = mvc.perform(put("/market/updateStockPrice/APPL"))
                        .andExpect(status().isOk())
                        .andReturn();
                String returnMsg = mvcResult.getResponse().getContentAsString();
                System.out.println(returnMsg);
                MvcResult mvcResult2 = mvc.perform(put("/market/updateStockPrice/TELSA"))
                        .andExpect(status().isOk())
                        .andReturn();
                String returnMsg2 = mvcResult2.getResponse().getContentAsString();
                System.out.println(returnMsg2);
                MvcResult mvcResult3 = mvc.perform(get("/portfolio/userId/1"))
                        .andExpect(status().isOk())
                        .andReturn();
                PortfolioSummaryVO portfolioSummaryVO = JSON.parseObject(mvcResult3.getResponse().getContentAsString(), PortfolioSummaryVO.class);
                System.out.println("\n## Portfolio");
                System.out.println(Tools.extendWithSpaces("Symbol", 25, false) +
                        Tools.extendWithSpaces("Price", 10, true) +
                        Tools.extendWithSpaces("Qty", 15, true) +
                        Tools.extendWithSpaces("Value", 15, true));
                List<PortfolioVO> portfolioVOList = portfolioSummaryVO.getPortfolioVOList();
                for (PortfolioVO portfolioVO : portfolioVOList) {
                    System.out.println(Tools.extendWithSpaces(portfolioVO.getTicker(), 25, false) +
                            Tools.extendWithSpaces(Tools.priceFormat.format(portfolioVO.getPrice()), 10, true) +
                            Tools.extendWithSpaces(Tools.priceFormat.format(portfolioVO.getPosition()), 15, true) +
                            Tools.extendWithSpaces(Tools.priceFormat.format(portfolioVO.getValue()), 15, true));
                }
                System.out.println();
            }
        }
    }
}

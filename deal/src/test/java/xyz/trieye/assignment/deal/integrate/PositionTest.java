package xyz.trieye.assignment.deal.integrate;

import com.alibaba.fastjson.JSON;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.trieye.assignment.deal.biz.asset.AssetDTO;
import xyz.trieye.assignment.deal.biz.asset.AssetDao;
import xyz.trieye.assignment.deal.biz.asset.SecurityTypes;
import xyz.trieye.assignment.deal.biz.position.vo.CommandVO;
import xyz.trieye.assignment.deal.biz.position.vo.PositionVO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PositionTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AssetDao assetDao;

    @Test
    void validateCommandVO_shouldValidateAllCases() throws Exception {
        CommandVO commandVO1 = CommandVO.builder().userId(-1).build();
        mvc.perform(post("/position/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(commandVO1)))
                .andExpect(content().string("The userId does not exist!"));
        CommandVO commandVO2 = CommandVO.builder().userId(1).build();
        PositionVO position = PositionVO.builder().ticker("notExist").securityType(SecurityTypes.STOCK).position(new BigDecimal(10)).build();
        commandVO2.setPositions(Arrays.asList(position));
        mvc.perform(post("/position/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(commandVO2)))
                .andExpect(content().string("Ticker notExist does not exist"));
    }

    @Test
    void when_position_request_validated_should_update_position() throws Exception {
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

        List<AssetDTO> assetDTOS = assetDao.queryByUserId(commandVO1.getUserId());
        assertThat(assetDTOS).hasSize(6)
                .extracting("securityId", "securityType", "position")
                .containsExactly(Tuple.tuple(1, SecurityTypes.STOCK, new BigDecimal(100)),
                        Tuple.tuple(1, SecurityTypes.OPTION, new BigDecimal(-2000)),
                        Tuple.tuple(2, SecurityTypes.OPTION, new BigDecimal(2000)),
                        Tuple.tuple(2, SecurityTypes.STOCK, new BigDecimal(-50)),
                        Tuple.tuple(3, SecurityTypes.OPTION, new BigDecimal(1000)),
                        Tuple.tuple(4, SecurityTypes.OPTION, new BigDecimal(-1000)));
    }
}

package xyz.trieye.assignment.deal.biz.position.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionVO {
    private String ticker;
    private String securityType;
    private BigDecimal position;
}

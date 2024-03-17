package xyz.trieye.assignment.deal.biz.portfolio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PortfolioVO {
    private String ticker;
    private BigDecimal price;
    private BigDecimal position;
    private BigDecimal value;
}

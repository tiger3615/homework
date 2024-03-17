package xyz.trieye.assignment.deal.biz.portfolio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PortfolioSummaryVO {
    private BigDecimal gross;
    private List<PortfolioVO> portfolioVOList;
}

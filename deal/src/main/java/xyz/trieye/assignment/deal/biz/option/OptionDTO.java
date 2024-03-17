package xyz.trieye.assignment.deal.biz.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO {
    private int id;
    private String ticker;
    private String type;
    private int stockId;
    private BigDecimal strike;
    private int maturity;
}

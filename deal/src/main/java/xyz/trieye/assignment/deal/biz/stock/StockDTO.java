package xyz.trieye.assignment.deal.biz.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private int id;
    private String ticker;
    private BigDecimal price;
    private BigDecimal annualDeviation;
}

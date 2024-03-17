package xyz.trieye.assignment.deal.biz.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDTO {
    private int id;
    private int userId;
    private String securityType;
    private int securityId;
    private BigDecimal position;
}

package xyz.trieye.assignment.deal.biz.position.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandVO {
    private int userId;
    private List<PositionVO> positions;
}

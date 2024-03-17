package xyz.trieye.assignment.deal.biz.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.trieye.assignment.deal.biz.position.vo.CommandVO;

@RestController
@RequestMapping("/position")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/place")
    public String updatePosition(@RequestBody CommandVO commandVO) {
        String validateResult = positionService.validateBiz(commandVO);
        if (!"OK".equals(validateResult)) {
            return validateResult;
        }
        positionService.updatePositions(commandVO);
        return "OK";
    }
}

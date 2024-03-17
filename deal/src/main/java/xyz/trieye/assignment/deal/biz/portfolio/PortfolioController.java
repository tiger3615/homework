package xyz.trieye.assignment.deal.biz.portfolio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/userId/{userId}")
    public PortfolioSummaryVO getPortfolioByUserId(@PathVariable int userId){
        return portfolioService.getPortfolioByUserId(userId);
    }
}

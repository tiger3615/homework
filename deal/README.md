# Formula
    JDK 8
    Spring Boot 2.5.15
    Mybatis 3.5.6
	Sqlite
    Other support libs
    
    
# Items are not implemented
- Did not validate data basically. e.g. ticker string length
- Just validate few Business attribute. e.g. just verify Stock ticker exist
# APIs 
  - /position/place
  
    Set position for Stock, Call, Put. Please refer PositionTest.java[PositionTest.java](src%2Ftest%2Fjava%2Fxyz%2Ftrieye%2Fassignment%2Fdeal%2Fintegrate%2FPositionTest.java).
 

- /market/updateStockPrice/{ticker}
    
    Simulate market to update stock price. Please refer [MarketTest.java](src%2Ftest%2Fjava%2Fxyz%2Ftrieye%2Fassignment%2Fdeal%2Fintegrate%2FMarketTest.java)
 

- /portfolio/userId/{userId}
    
    Check specific user's portfolio. Please refer [PortfolioTest.java](src%2Ftest%2Fjava%2Fxyz%2Ftrieye%2Fassignment%2Fdeal%2Fintegrate%2FPortfolioTest.java)
    

# Sample Real-time Output
There is demo() test case in PortfolioTest.java has no assert, just for demonstrating the "Sample Real-time Output"
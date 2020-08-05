Feature: Search for the flights


@firefox
  Scenario Outline: Search for the Cheapest and Fastest Flights
    Given Open the browser "firefox" and navigate to the Flight Booking "https://www.makemytrip.com/flights/" 
    
    Then Enter "<source>" and "<destination>"
    Then Select Date
    And Click on the search button
    Then Findout the cheapest and Fastest Flight
  
    
    
     Examples:  
     | source  | destination | 
     | Hyderabad |     Mumbai |
     | Delhi |     Hyderabad |
     
  
    
    
     
    
   
    
    


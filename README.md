# OZAN FX

##Quick install
-Clone the repository and extract app.rar in any desired location
-Create a corresponding database according to application.properties which is located under config folder
-Double click start.bat and wait until console logs "Ready to go"

##With Source Code
- Clone the repository into your ide
- Create a corresponding database according to application.properties which is located under config folder
- Run OzRestApi under ozan-rest module

##Using API's
- Import ozan-rest.json collection into postman
- Start using the 3 api's

##Centralized Logging
- There are 4 log files: application.log which contains all of the logs, jpa.log, backend.log and rest.log instead contains the logs according to modules.
- All the logs are in debug level and they will be fragmented if they exceed 10 MB
- In order to change logging configuration, you should manipulate spring-logback.xml
- spring-logback.xml file is located under config folder ozan-rest module
- With quick install, you can find the same file under config folder where there is also application.properties

##Unit Tests
- There exists 8 integration tests under ozan-rest module test path which also covers error handling

##Data Sync
- I used rates-api to fetch exchange rates which use ECB as datasource
- ECB publish daily exchange rates each day around 16:00 CET
- Ozan FX Application syncs exchange rates at 17:00 CET every week-day and once when it runs very first time

##Performance
- Application operates with too small data for opening db connections for each transaction
- Therefore latest exchange rates are all kept in memory for performance
- In order to not loose historical exchange rate data and user transactions, they also are all persisted in DB

##Note
- I did not prefer soft-delete feature considering this prod never would be live, even my data pattern is fully relational
- I would recommend to create partition in oz_rate and oz_conversion tables according to date column if you would like to test it with bigger data-set


Happy Tests..
# Sosyal Yazılım

## Quick install
- Clone the repository and extract app.rar in any desired location
- Create a corresponding database according to application.properties which is located under config folder
- Double click start.bat and wait until console logs "Ready to go!"

##With Source Code
- Clone the repository into your ide
- Create two corresponding databases according to application.properties and test.properties which are located under config folder
- Run SyRestApi under sy-rest module

## Using API's
- Import postman.json collection into postman
- Start using the 12 APIs

## Centralized Logging
- There are 4 log files: application.log which contains all of the logs, jpa.log, backend.log and rest.log instead contains the logs according to modules.
- All the logs are in debug level and they will be fragmented if they exceed 10 MB
- In order to change logging configuration, you should manipulate spring-logback.xml
- spring-logback.xml file is located under config folder of sy-rest module
- With quick install, you can find the same file under config folder where there is also application.properties

## Unit Tests
- There exists 4 integration tests under sy-rest module test path which covers also some error handling

## Note
- I did not prefer soft-delete feature considering this prod never would be live, even my data pattern is fully relational
- I would recommend to create partition in sy_contract table according to year column if you would like to test it with bigger data-set


Happy Tests..
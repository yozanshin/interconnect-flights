# interconnect-flights
Java/Spring based API which serves information about possible direct and interconnected flights (EMF)

Developed by: Enrique Moya Frutos

NOTES:

There are two versions in the following Maven modules:

- ryanair-task2-boot -> Spring Boot executable JAR with embedded Tomcat server (microservices oriented architecture approach)
- ryanair-task2-war -> Spring Boot deployable WAR into any JEE (Servlet 3.0) server (classic JEE architecture approach)

Both will generate the given JAR or WAR file by executing "mvn clean package" Maven command

RyanAir API endpoints (Routes and Schedules) are parametrized in the "application.yml" file and can be also set by command line variables and other ways of setting environment variables

In my opinion there is actually a performance issue related to hte number of HTTP requests done in interconnection with one stop.
Performance should be improved in order to get the user experience better and avoid timeout issues. In my opinion, next step would be improve the performance by storing in cache some Schedules and so liit the number of HTTP requestes.
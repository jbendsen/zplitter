# zplitter
A sample application for settling expenses in a group op people

## Tech stack
This application is built with Spring Boot, Kotlin, Gradle and exposes a RESTful API.

## Running
* Build the application using gradle:> _gradle build_
* Run the java class: _dk/lundogbendsen/demo/zplitter/ZplitterApplication_
* The spring boot server will then start up. See the endpoints section for exposed endpoints. 

## Endpoints
* [Swagger UI documentation](http://localhost:8080/swagger-ui-custom.html)
* [Swagger JSON document](http://localhost:8080/v3/api-docs)
* [All persons](http://localhost:8080/api/persons)
* [All events](http://localhost:8080/api/events)

## See
[HELP.md](./HELP.md)
# GoodTripServer

## Description
Server for Android application GoodTrip.

## Dependencies
- JDK 21
- JUnit 4
- [**Lombok**](https://projectlombok.org/) for getters and setters
- [**Spring**](https://spring.io) Boot 3
- [**PostgreSQL**](https://www.postgresql.org)

## Build and Run
To build and run the project from the command line:

1) Set properties in the next files:

   `application.properties`
   
        PLACES_API_KEY                 # key from google places api
        FLIGHT_API_KEY                 # public key from amadeus api
        FLIGHT_API_SECRET_KEY          # private key from amadeus api
        spring.datasource.url          # database url
        spring.datasource.username     # database username
        spring.datasource.password     # database password

   `encryption.properties`

         salt                          # your private salt of application to hash passwords

3) Enter commands in the command line:

       $ ./gradlew build                        
       $ ./gradlew run

4) If you want to test application enter:

       $ ./gradlew test


## Links

1) [**Google Places API**](https://developers.google.com/maps/documentation/places/web-service/overview?hl=ru) - to get places nearby
2) [**Amadeus API**](https://developers.amadeus.com) - 
to get a list of air tickets

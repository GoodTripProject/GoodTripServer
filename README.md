# GoodTripServer

## Description

Back-end part of the Android application [GoodTrip](https://github.com/GoodTripProject/GoodTripAndroid).

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

2) Enter commands in the command line:

       $ ./gradlew build                        
       $ ./gradlew run

## Test
If you want to test application enter:

1) Set properties as in the previous paragraph

2) Enter command in the command line:


       $ ./gradlew test

## Documentation

- Raw swagger [documentation](https://app.swaggerhub.com/apis-docs/SHTEINMAKS/GoodTrip/1.0.0)

## Getting in touch
If you have issues/questions/problems, you can write:
- [Andrei Bolotskii](https://github.com/andrewbolotsky) - andrew04tlt@gmail.com
- [Maksim Shtein](https://github.com/MaksimkaSH) - shteinmaks@gmail.com

## Resources

- [**Google Places API**](https://developers.google.com/maps/documentation/places/web-service/overview?hl=ru) - to get places nearby
- [**Amadeus API**](https://developers.amadeus.com) - 
to get a list of air tickets

## License

GoodTrip is an Open Source project covered by the [MIT License](LICENSE). 

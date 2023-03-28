# leaderboard
API to design and implement a simple leaderboard system for a social application. 

<hr>

* [Spring Boot 3.0.5](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## [Swagger](http://localhost:8080/swagger-ui/index.html)

# Build & Run

## build and execute jar with JDK 17
```bash
./gradlew build
java -jar build/libs/leaderboard-{version}.jar
```

## run spring-boot application
```bash
./gradlew bootRun
```

## run tests
```bash
./gradlew test
```

## build OCI image
```bash
docker build --no-cache=true -t leaderboard .
```

## run the image inside as a container
```bash
docker run -d -p 8080:8080 leaderboard
```
# leaderboard
API to design and implement a simple leaderboard system for a social application.

<hr>

* [Spring Boot 3.0.5](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## [Swagger](http://localhost:8080/swagger-ui/index.html)

# implementation
This branch is the beginning of the implementation of a much more efficient API to handle 1M concurrent requests.
This implementation take advantage of the data structure Priority Blocking Queue (which is thread safe and can 
handle concurrent requests) which provides insertion with logarithmic time complexity O(log n), peek the higher 
element is constant time. So every time that a user request a new user score, the score is added in the priority 
queue with O(log(n)). Also, we can avoid the priority queue to increase the size, keeping it always with the 
leaderboard of only the higher 10 elements. Since the size of the Priority Queue will be limited with 10 elements 
and keeping other scores is not necessary, inserting and getting the leaderboard is constant time.

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

## For building OCI image and running it as a container, simply use docker-compose:
```bash
docker-compose up --build
```

## For scaling up the API for 3 servers (for example) simply use docker-compose with the docker-compose-scale.yml:
```bash
docker-compose --file docker-compose-scale.yml up -d --build --scale leaderboard-server=3 
```

## Alternatively, to build OCI image and run the image inside as a container
```bash
docker build -t leaderboard .
docker run -d -p 8080:8080 leaderboard
```
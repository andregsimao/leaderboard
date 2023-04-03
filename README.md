# leaderboard
API to design and implement a simple leaderboard system for a social application.

<hr>

* [Spring Boot 3.0.5](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## [Swagger](http://localhost:8080/swagger-ui/index.html)

## Implementation
It is an API that take advantage of the data structure Priority Blocking Queue (which is thread safe and can 
handle concurrent requests) which provides insertion with logarithmic time complexity O(log n), peek the higher
element is constant time. So every time that a user request a new user score, the score is added in the priority 
queue with O(log(n)). Also, we can avoid the priority queue to increase the size, keeping it always with the 
leaderboard of only the higher 10 elements. Since the size of the Priority Queue will be limited with 10 elements 
and keeping other scores is not necessary, inserting and getting the leaderboard is constant time.

## Endpoint to add new user. Examples:

### POST request to localhost:8080/leaderboard/add-new-user

### Input

```json
{
  "username": "test-user",
  "score": 4
}
```

### Output
Status code 200 and a success message in the response body

```
{
  "Success adding new Score: ScoreRequest(username=ereretr, score=4)"
}
```

### Input

```json
{
  "username": "  ",
  "score": 4
}
```

### Output
Status code 400 and an error message because the username cannot be blank

```
{
  "Exception while adding new user score for input ScoreRequest(username= , score=4): Username should not be empty"
}
```

## Endpoint to get leaderboard. Examples:

### all time leaderboard: GET request to localhost:8080/leaderboard/all-time-leaderboard
### monthly leaderboard: GET request to localhost:8080/leaderboard/monthly-leaderboard

### Output
Status code 200
```json
{
  "message": "Leaderboard built with success",
  "referenceMonth": null,
  "leaderBoard": [
    {
      "username": "eee",
      "score": 20
    },
    {
      "username": "eee",
      "score": 20
    },
    {
      "username": "eee",
      "score": 20
    },
    {
      "username": "eee",
      "score": 20
    },
    {
      "username": "tytyt",
      "score": 6
    },
    {
      "username": "ereretr",
      "score": 6
    },
    {
      "username": "ereretr",
      "score": 6
    },
    {
      "username": "ereretr",
      "score": 6
    },
    {
      "username": "string",
      "score": 0
    },
    {
      "username": "string1",
      "score": -2
    }
  ]
}
```


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
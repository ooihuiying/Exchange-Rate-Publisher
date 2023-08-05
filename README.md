## What is this
- Get exchange rate from source: https://app.dataforseo.com/api-detail/serp
- Publish exchange rate to Kafka topic
- Consume from Spark and do processing
- Display result in UI (index.html)

### Prerequisite
- Use Java 11. Don't use Java 17 because I faced some problems with Spark.....

### To run with docker image
- In `application.properties`, set `spring.kafka.bootstrap-servers=kafka:9092`
- `sudo service docker start` use this command if you are on linux. If you are on MacOs, just start the Docker desktop.
- Run `docker-compose build` then `docker-compose up` to start Zookeeper, Kafka and Spark.
-  If you only want to run the Docker image, run `docker build -t IMAGE_NAME .` and `docker run -p 8080:8080 IMAGE_NAME`
- `docker-compose down -v` to shut down docker
- If you want to see messages in kafka Topic, `docker-compose exec kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic TOPIC_NAME --from-begin
  ning`

### To run without docker
- You can do this if you have installed Java, Kafka and Spark in your local computer.
- Kafka version: kafka_2.13-3.5.1
- In `application.properties`, set `spring.kafka.bootstrap-servers=localhost:9092`.
- [first terminal] Start Zookeeper (included in Kafka) by navigating to Kafka folder and run `bin/zookeeper-server-start.sh config/zookeeper.properties`
- [second terminal] Start Kafka by navigating to Kafka folder and run `bin/kafka-server-start.sh config/server.properties`
- [third terminal] To check that messages are published to Kafka topic, navigate to the downloaded Kafka in your local computer and run `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic TOPIC_NAME --from-beginning`
- Now, using IDE or running gradle commands, run the project DemoApplication with Java 11 version.
- Open browser `http://localhost:8080/historicalExchange` to start to publish messages to Kafka Topic.
- This will be picked up by Spark and then sent through Websocket and displayed in the UI at localhost:8080/.
- Alternatively, see the messages printed in the third terminal to verify that the messages are published properly.

## Helpful debug tips
- If Kafka has issues, just kill it by `lsof -i :9092` to get the pid and then `kill -9 <PID>`
- Remember that the DockerFile should contain the same dependency versions as that defined in build.gradle
- Currently, Spark is expected to be running in LOCAL mode, not CLUSTER mode.
- [Helpful Websocket resource](https://www.section.io/engineering-education/getting-started-with-spring-websockets/)
## What is this
- Get exchange rate from source: https://app.dataforseo.com/api-detail/serp
- Publish exchange rate to Kafka topic
- Consume from Spark and do processing

### Prerequisite
- Use Java 11. We don't use Java 17 because I faced some problems with Spark.....

### To run with docker image
- `sudo service docker start` use this command if you are on linux. If you are on MacOs, just start the Docker desktop.
- `./gradlew clean build` to generate JAR file under `build/libs/demo-0.0.1-SNAPSHOT.jar`
- `docker-compose build` then `docker-compose up` to start Zookeeper, Kafka and Spark
-  If you only want to run the Docker image, run `docker build -t IMAGE_NAME .` and `docker run -p 8080:8080 IMAGE_NAME`
- `docker-compose down -v` to shut down 

### To run without docker
- You can do this if you have installed Java, Kafka and Spark in your local computer.
- Kafka version: kafka_2.13-3.5.1
- [first terminal] Start Zookeeper (included in Kafka) by navigating to Kafka folder and running `bin/zookeeper-server-start.sh config/zookeeper.properties`
- [second terminal] Start Kafka by navigating to Kafka folder and running `bin/kafka-server-start.sh config/server.properties`
- [third terminal] To check the data in Kafka, navigate to Kafka folder and run `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic exchange-rates --from-beginning`
- Remember to check the Kafka configurations in the code that it is connected to localhost:9092. See `KafkaProducer` and `application.properties`
- Now, using IDE or running gradle commands, run the project DemoApplication wth Java 11 version.
- Open browser `http://localhost:8080/historicalExchange`. This endpoint will publish messages to Kafka. 
- See the messages printed in the third terminal to verify message is published properly.
- Alternatively, just go to localhost:8080/ to see the UI

## Helpful debug tips
- If Kafka has issues, just kill it by `lsof -i :9092` to get get the pid and then `kill -9 <PID>`
## What is this
- When this application is started, user will need to go to the UI(index.html) and input the currency type, eg, AUD
- This will trigger a read of a CSV file: https://www.kaggle.com/datasets/ruchi798/currency-exchange-rates
- Data for the selected currency type will be published to a Kafka topic. Eg, if user selected AUD currency, relevant rows will be published to AUD topic
- There is a listening Spark consumer (currently run in local mode) which will continually listen for all Kafka topics and then publish to a Websocket topic
- Results will be displayed in the UI

### Prerequisite
- Use Java 11. Don't use Java 17 because I faced some problems with Spark.....

### Some explanation of the code
- Controller: contains endpoints for clients to interact with the app.
- ExchangeRateCsvParser: Contains logic to read from CSV file. Implement sleep to simulate real-life data processing and event arrivals.
- KafkaProducer: Contains code to produce messages to queue. Each Currency type has a Topic.
- MessageHandler: Used to communicate with Websocket in the UI.
- SparkConsumer: Contains code to read from Kafka. Uses a library to connect both tools. Emits data to Websocket in the UI.
- Index.html and App.js: Frontend code.
- [Video Explanation](https://drive.google.com/file/d/1kty7pBQ__d_nN9nf3bDvBNNNEgELWqsA/view?usp=sharing)

  
![Preview](https://github.com/ooihuiying/Exchange-Rate-Publisher/assets/40648338/0c4d35fe-bb80-472c-a174-3057525f87b4)



### To start up with docker image
- In `application.properties`, set `spring.kafka.bootstrap-servers=kafka:9092`
- `sudo service docker start` use this command if you are on linux. If you are on MacOs, just start the Docker desktop.
- Run `docker-compose build` then `docker-compose up` to start Zookeeper, Kafka and Spark.
-  If you only want to run the Docker image, run `docker build -t IMAGE_NAME .` and `docker run -p 8080:8080 IMAGE_NAME`
- `docker-compose down -v` to shut down docker
- If you want to see messages in kafka Topic, `docker-compose exec kafka kafka-console-consumer --bootstrap-server kafka:9092 --topic TOPIC_NAME --from-begin
  ning`  

### To start up without docker
- You can do this if you have installed Java, Kafka and Spark in your local computer.
- Kafka version: kafka_2.13-3.5.1
- In `application.properties`, set `spring.kafka.bootstrap-servers=localhost:9092`.
- [first terminal] Start Zookeeper (included in Kafka) by navigating to Kafka folder and run `bin/zookeeper-server-start.sh config/zookeeper.properties`
- [second terminal] Start Kafka by navigating to Kafka folder and run `bin/kafka-server-start.sh config/server.properties`
- [third terminal] To check that messages are published to Kafka topic, navigate to the downloaded Kafka in your local computer and run `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic TOPIC_NAME --from-beginning`
- Now, using IDE or running gradle commands, run the project DemoApplication with Java 11 version.

### To see output after starting up
- Open the browser `http://localhost:8080` to load the UI. This will trigger the work to publish messages to Kafka Topic.
- This will be picked up by Spark and then sent through Websocket and displayed in the UI at localhost:8080/.
- Alternatively, see the messages printed in the third terminal to verify that the messages are published properly.

## Helpful debug tips
- If Kafka has issues, just kill it by `lsof -i :9092` to get the pid and then `kill -9 <PID>`
- Remember that the DockerFile should contain the same dependency versions as that defined in build.gradle
- Currently, Spark is expected to be running in LOCAL mode, not CLUSTER mode.
- [Helpful Websocket resource](https://www.section.io/engineering-education/getting-started-with-spring-websockets/)

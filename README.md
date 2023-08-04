- Get exchange rate from source
- Publish exchange rate to Kafka topic
- Consume from Spark

### To run
- `sudo service docker start` use this command if you are on linux. If you are on MacOs, just start the Docker desktop.
- `./gradlew clean build` to generate JAR file under `build/libs/demo-0.0.1-SNAPSHOT.jar`
- `docker-compose build` then `docker-compose up` to start Zookeeper, Kafka and Spark
-  If you only want to run the Docker image, run `docker build -t IMAGE_NAME .` and `docker run -p 8080:8080 IMAGE_NAME`
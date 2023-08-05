# Use an official OpenJDK 11 base image
FROM openjdk:11-jdk

# Set the working directory
WORKDIR /app

# Install dependencies: Apache Spark and Kafka (example)
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/spark/spark-3.1.2/spark-3.1.2-bin-hadoop3.2.tgz && \
    tar -xvzf spark-3.1.2-bin-hadoop3.2.tgz && \
    mv spark-3.1.2-bin-hadoop3.2 /opt/spark && \
    wget https://downloads.apache.org/kafka/3.5.1/kafka-3.5.1-src.tgz && \
    tar -xvzf kafka-3.5.1-src.tgz && \
    mv kafka-3.5.1-src /opt/kafka && \
    rm spark-3.1.2-bin-hadoop3.2.tgz kafka-3.5.1-src.tgz && \
    apt-get remove -y wget && \
    apt-get autoremove -y && \
    apt-get clean

# Set environment variables for Spark and Kafka
ENV SPARK_HOME=/opt/spark
ENV PATH=$PATH:$SPARK_HOME/bin
ENV KAFKA_HOME=/opt/kafka
ENV PATH=$PATH:$KAFKA_HOME/bin

# Copy the Gradle wrapper files to the container
COPY gradlew .
COPY gradle gradle

# Copy the Gradle build files to the container
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

RUN ./gradlew clean build

RUN ./gradlew bootJar

# Expose the necessary ports
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "/app/build/libs/demo-0.0.1-SNAPSHOT.jar"]

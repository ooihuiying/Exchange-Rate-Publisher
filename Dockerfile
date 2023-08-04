# Use an official OpenJDK base image
FROM ubuntu:20.04

# Set the working directory
WORKDIR /app

# Update package list and install OpenJDK 17
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-17-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Install dependencies: Apache Spark and Kafka (example)
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://dlcdn.apache.org/spark/spark-3.4.1/spark-3.4.1-bin-hadoop3.tgz && \
    tar -xvzf spark-3.4.1-bin-hadoop3.tgz && \
    mv spark-3.4.1-bin-hadoop3 /opt/spark && \
    wget https://downloads.apache.org/kafka/3.5.1/kafka-3.5.1-src.tgz && \
    tar -xvzf kafka-3.5.1-src.tgz && \
    mv kafka-3.5.1-src /opt/kafka && \
    rm spark-3.4.1-bin-hadoop3.tgz kafka-3.5.1-src.tgz && \
    apt-get remove -y wget && \
    apt-get autoremove -y && \
    apt-get clean

# Set environment variables for Spark and Kafka
ENV SPARK_HOME=/opt/spark
ENV PATH=$PATH:$SPARK_HOME/bin
ENV KAFKA_HOME=/opt/kafka
ENV PATH=$PATH:$KAFKA_HOME/bin

# Expose the necessary ports
EXPOSE 8080

# Copy the compiled Spring Boot JAR file from the build output directory
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

# Command to run the Spring Boot application
CMD ["java", "-jar", "/app/demo-0.0.1-SNAPSHOT.jar"]

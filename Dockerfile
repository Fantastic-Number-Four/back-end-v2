FROM openjdk:8-jdk-alpine

COPY /target/project2-fantastic-forex-backend.jar project2-fantastic-forex-backend.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "project2-fantastic-forex-backend.jar"]

# docker build -t my-api:auto .
# docker run -d -p 5000:5000 my-api:auto
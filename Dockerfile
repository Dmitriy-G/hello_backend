FROM java:openjdk-8-jdk-alpine

COPY /target/hello_backend-*.jar /app.jar

ENTRYPOINT ["java","-jar", "app.jar"]

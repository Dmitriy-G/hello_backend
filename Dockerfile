FROM java:openjdk-8-jdk-alpine

ENTRYPOINT ["java","-jar", "hello_backend-0.0.1-SNAPSHOT.jar"]

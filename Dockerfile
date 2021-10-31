FROM openjdk:11
LABEL description="API - Credit Account"
LABEL maintainer="Ronivon Sampaio"
ADD target/credit-account-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]
EXPOSE 8080

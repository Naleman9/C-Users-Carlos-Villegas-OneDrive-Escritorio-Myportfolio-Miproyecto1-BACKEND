FROM amazoncorretto:11-alpine-jdk
MAINTAINER nab
COPY target/nab-0.0.1-SNAPSHOT.jar nab-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/nab-0.0.1-SNAPSHOT.jar"]

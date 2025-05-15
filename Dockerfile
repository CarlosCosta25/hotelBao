FROM openjdk:21
WORKDIR /app

COPY target/hotelBao-0.0.1-SNAPSHOT.jar /app/hotelBao.jar


LABEL authors="carlos-costa"

ENTRYPOINT ["java", "-jar", "hotelBao.jar"]
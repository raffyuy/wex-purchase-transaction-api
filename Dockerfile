FROM amazoncorretto:17-alpine-jdk
# VOLUME /tmp
WORKDIR /app
ARG JAR_FILE=build/libs/purchase-transaction-api-1.0.0.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

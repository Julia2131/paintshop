#FROM eclipse-temurin:17-jre-alpine
#ARG JAR_FILE=target/*.jar
## Set working directory
#WORKDIR /app
## Copy Maven build result
#COPY ${JAR_FILE} app.jar
## Expose port
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy tất cả file jar và đổi tên
COPY target/paintmanagement-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

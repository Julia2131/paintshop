
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy tất cả file jar và đổi tên
COPY target/paintmanagement-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

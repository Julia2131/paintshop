# Sử dụng JDK 17 (hoặc 11 tùy version cậu dùng)
FROM eclipse-temurin:17-jdk-alpine

# Tạo thư mục chứa app
WORKDIR /app

# Copy file jar đã build vào image
COPY target/*.jar app.jar

# Chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]

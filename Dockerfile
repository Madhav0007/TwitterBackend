# Stage 1 - Build the JAR
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy maven wrapper and pom first (better layer caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Download dependencies separately (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2 - Run the JAR (smaller final image)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create uploads directory
RUN mkdir -p uploads

# Copy JAR from build stage
COPY --from=builder /app/target/task-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
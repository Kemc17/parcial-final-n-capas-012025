# Usa JDK 17 slim
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copia el jar generado en target
COPY target/parcial-final-n-capas-0.0.1-SNAPSHOT.jar app.jar

# Expone puerto de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
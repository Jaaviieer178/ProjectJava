# Usamos una imagen oficial de Java como base
FROM eclipse-temurin:21-jdk-alpine

# Creamos un directorio para la app
WORKDIR /app

# Copiamos el .jar generado
COPY target/java-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
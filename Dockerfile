# =============================================
# ETAPA 1: Construcción (Build)
# Usamos una imagen Maven con Java 17 para compilar
# =============================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el pom.xml primero (para aprovechar el caché de Docker)
COPY pom.xml .

# Descargamos las dependencias sin compilar el código aún
RUN mvn dependency:go-offline -B

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos y empaquetamos la aplicación (sin correr tests aquí)
RUN mvn clean package -DskipTests

# =============================================
# ETAPA 2: Ejecución (Runtime)
# Usamos una imagen más liviana solo con Java 17
# =============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos solo el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 (el que usa Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

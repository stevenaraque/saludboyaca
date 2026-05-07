# ============================================
# ETAPA 1: COMPILACIÓN (Usando imagen oficial de Maven y Java 17)
# ============================================
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

# Copiamos el pom y descargamos dependencias para aprovechar el caché
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código y compilamos el .war
COPY src ./src
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: EJECUCIÓN (Tomcat 9 optimizado para Java 17)
# ============================================
FROM tomcat:9.0-jdk17-temurin

# Limpiamos las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el .war generado y lo renombramos a ROOT.war
COPY --from=build /app/target/saludboyaca-1.0.war /usr/local/tomcat/webapps/ROOT.war

# Variables de entorno para la aplicación
ENV DB_URL="jdbc:mysql://mysql:3306/saludboyaca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
ENV DB_USER="root"
ENV DB_PASS="root123"
ENV EMAIL_REMIT="tucorreo@gmail.com"
ENV EMAIL_PASS="xxxx xxxx xxxx xxxx"

EXPOSE 8080

CMD ["catalina.sh", "run"]
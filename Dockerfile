# ============================================
# ETAPA 1: COMPILACIÓN
# ============================================
FROM eclipse-temurin:11-jdk-alpine AS build

# Instalar Maven manualmente
RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: EJECUCIÓN
# ============================================
FROM tomcat:9.0-jdk11-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/saludboyaca-1.0.war /usr/local/tomcat/webapps/ROOT.war

ENV DB_URL="jdbc:mysql://mysql:3306/saludboyaca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
ENV DB_USER="root"
ENV DB_PASS="root123"
ENV EMAIL_REMIT="tucorreo@gmail.com"
ENV EMAIL_PASS="xxxx xxxx xxxx xxxx"

EXPOSE 8080

CMD ["catalina.sh", "run"]
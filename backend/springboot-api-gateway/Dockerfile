FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Cache common-service dependencies, then install it
COPY springboot-common-service/pom.xml common-service/pom.xml
RUN mvn -f common-service/pom.xml dependency:go-offline -q
COPY springboot-common-service/src common-service/src
RUN mvn -f common-service/pom.xml install -DskipTests -q

# Cache service dependencies, then build
COPY springboot-api-gateway/pom.xml service/pom.xml
RUN mvn -f service/pom.xml dependency:go-offline -q
COPY springboot-api-gateway/src service/src
RUN mvn -f service/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/service/target/*.jar app.jar
EXPOSE 8765
ENTRYPOINT ["java", "-jar", "app.jar"]

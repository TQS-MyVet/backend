FROM maven:3.9-eclipse-temurin-21-alpine

WORKDIR /myvet

COPY . .

RUN mvn install -DskipTests

CMD mvn spring-boot:run
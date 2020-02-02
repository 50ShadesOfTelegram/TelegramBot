FROM openjdk:8-jdk as build

RUN apt update
RUN apt install -y maven curl

WORKDIR /bot

COPY src src
COPY pom.xml pom.xml

RUN mvn package

FROM openjdk:8-jre-alpine

WORKDIR /bot

COPY --from=build /bot/target/telegram-bot-1.0-SNAPSHOT.jar bot.jar
COPY config.json config.json

HEALTHCHECK CMD curl --fail http://localhost:8080/health || exit 1
CMD java -jar bot.jar
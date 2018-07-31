FROM openjdk:8-jdk as build

RUN apt update
RUN apt install -y maven

WORKDIR /bot

COPY src src
COPY pom.xml pom.xml

RUN mvn package

FROM openjdk:8-jre-alpine

ARG BOT_API_KEY
ENV BOT_API_KEY ${BOT_API_KEY}

WORKDIR /bot

COPY --from=build /bot/target/telegram-bot-1.0-SNAPSHOT.jar bot.jar

CMD java -jar bot.jar "${BOT_API_KEY}"
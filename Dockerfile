FROM openjdk:11.0-slim

WORKDIR /bot

COPY ./target/telegram-bot-1.0-SNAPSHOT.jar bot.jar
HEALTHCHECK CMD curl --fail http://localhost:8080/health || exit 1
CMD java -jar bot.jar
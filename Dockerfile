FROM openjdk:17-buster as builder
COPY /telegram-bot.jar /telegram-bot.jar
EXPOSE 8080
CMD ["java", "-jar", "/telegram-bot.jar"]

FROM openjdk:17-buster as builder
COPY build/libs/telegram.rent.bot-1.0.jar /telegram.bot.jar
CMD ["java", "jar", "/telegram.bot.jar"]

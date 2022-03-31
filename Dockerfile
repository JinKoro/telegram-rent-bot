FROM openjdk:17-buster as builder
COPY /home/jin/telegram-bot.jar /telegram-bot.jar
CMD ["java", "jar", "/telegram.bot.jar"]

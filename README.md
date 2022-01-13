# Rent Telegram Bot

## Configuration

Create ```local.properties``` in the resources of src/main and put there the following settings

```
network.telegram.bot.token = <bot_token>
network.telegram.bot.name = <bot_name>
```

## Useful commands

#### Format code [Setup Ide Code style](https://github.com/pinterest/ktlint#option-3)
```bash
./gradlew detekt
```

#### Update dependencies
```bash
./gradlew dependencyUpdates
```

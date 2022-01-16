# Telegram Rent Bot

The rent bot can publish post or messages to different channels that connect to this bot. 
Due to telegram restriction, publications occur no more than 20 post in a minute.
Rent bot provides 3 site with apartment announcements: 
 - Onliner
 - Kufar
 - Realt

## Configuration

Create ```local.properties``` in the resources of src/main and put there the following settings

```
telegram.rent.bot.token = <bot_token>
telegram.rent.channels = <channel_name>, <channel_name_2>
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

## Example

You can add your commands in different class that implement ```CommandHandler()```
```
    init {
        command("hello") {
            sendMessage(
                text = "Hello User!", 
                channels = builder.channels
            )
        }  
    } 
```

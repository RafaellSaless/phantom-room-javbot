# Phantom Room JavBot

[Português (Brasil)](README.pt-br.md)

A clean and modular Java Discord bot focused on managing temporary voice channels and organizing gaming sessions.

> **Project status:** Under development

## Overview

Phantom Room JavBot is a Discord bot developed in Java with the goal of providing an automated temporary voice channel system and gameplay management features.

The core concept is simple: users join a designated voice channel, and the bot automatically creates a temporary room for them. When the room is no longer being used, it can be removed automatically, keeping the server organized and reducing unnecessary channels.

The project also aims to provide a gameplay scheduling system, allowing users to organize gaming sessions, manage participants, and automatically create private voice channels for each gameplay.

The project is being developed with a modular architecture, allowing new commands and features to be added over time.

## Current Status

The project is currently under development.

The repository is being built incrementally, with each branch representing a specific stage or feature of the development process.

### Implemented

* Initial project structure
* Basic Discord bot configuration
* Environment variable configuration
* Command architecture
* Temporary voice channel creation
* Temporary voice channel deletion
* Channel configuration
* Permission management

### In Development

* Gameplay scheduling system

### Planned

* Gameplay creation through commands
* Gameplay information management
* Gameplay announcements
* Gameplay participant management
* Private gameplay voice channels
* Join and leave gameplay controls
* Player limit management
* Gameplay cancellation
* Automatic gameplay channel management
* Custom temporary channel names
* User-specific channel controls
* Help command
* Additional server management features

## Gameplay Management

The gameplay management system will allow server members to organize gaming sessions directly through the bot.

A user will be able to create a gameplay by providing information such as:

* Game name
* Maximum number of players
* Scheduled time

The bot will then publish the gameplay in a channel configured specifically for gameplay announcements.

Each gameplay will have interactive controls allowing users to join or leave the session.

When a user joins a gameplay, the bot will automatically grant them permission to access the private voice channel created for that session.

### Example

A gameplay could be created with the following information:

```text
Game: PEAK
Players: 4
Time: 22:00
```

The bot will publish the gameplay information in the configured channel and create a private voice channel for the session, for example:

```text
🔒・RafaelSales — PEAK
```

Users can join the gameplay through the available controls. Once they join, they automatically receive access to the corresponding private voice channel.

The system will also keep track of the number of participants, preventing additional users from joining when the maximum player limit has been reached.

## Tech Stack

| Technology     | Purpose                           |
| -------------- | --------------------------------- |
| Java 17+       | Main programming language         |
| JDA            | Discord API integration           |
| Maven / Gradle | Build and dependency management   |

## Configuration

The bot uses environment variables for sensitive configuration data.

Create a `.env` file in the project root:

```env
DISCORD_TOKEN=your_bot_token_here
PREFIX=your_bot_prefix_here
```

Never commit your bot token or other sensitive credentials to the repository.

## Discord Intents

The bot requires the following intents to be enabled in the Discord Developer Portal:

* `SERVER MEMBERS INTENT`
* `MESSAGE CONTENT INTENT`
* `GUILD VOICE STATES`

## Development

The project is being developed incrementally through separate Git branches.

The `base-config` branch contains the initial project configuration and serves as the foundation for subsequent development.

As new features are implemented, they will be developed in dedicated branches and merged into the main project.

## Roadmap

* [x] Initial project configuration
* [x] Command architecture
* [x] Temporary voice channel creation
* [x] Temporary voice channel deletion
* [x] Channel configuration
* [x] Permission management
* [ ] Gameplay scheduling
* [ ] Gameplay announcements
* [ ] Gameplay participant management
* [ ] Private gameplay voice channels
* [ ] Join and leave gameplay controls
* [ ] Player limit management
* [ ] Gameplay cancellation
* [ ] Automatic gameplay management
* [ ] Additional server management features
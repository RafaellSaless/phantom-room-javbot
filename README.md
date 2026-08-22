# Phantom Room JavBot

[Português (Brasil)](README.pt-br.md)

A clean and modular Java Discord bot focused on managing temporary voice channels.

> **Project status:** Under development

## Overview

Phantom Room JavBot is a Discord bot developed in Java with the goal of providing an automated temporary voice channel system.

The core concept is simple: users join a designated voice channel, and the bot automatically creates a temporary room for them. When the room is no longer being used, it can be removed automatically, keeping the server organized and reducing unnecessary channels.

The project is being developed with a modular architecture, allowing new commands and features to be added over time.

## Current Status

The project is currently under development.

The repository is being built incrementally, with each branch representing a specific stage or feature of the development process.

### Implemented

* Initial project structure
* Basic Discord bot configuration
* Environment variable configuration
* Initial voice channel system structure

### In Development

* Temporary voice channel creation
* Temporary channel management
* Command system
* Server configuration commands

### Planned

* Permission management
* Custom temporary channel names
* User-specific channel controls
* Help command
* Additional moderation and management features

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
* [ ] Command architecture
* [ ] Temporary voice channel creation
* [ ] Temporary voice channel deletion
* [ ] Channel configuration
* [ ] Permission management
* [ ] Additional server management features

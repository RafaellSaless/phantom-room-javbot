# Phantom Room JavBot

[Português (Brasil)](README.pt-br.md)

A clean and modular Java Discord bot focused on server management, temporary voice channels, ticket management, and gameplay organization.

> **Project status:** Under development

## Overview

Phantom Room JavBot is a Discord bot developed in Java with the goal of providing automated server management features focused on voice channels, tickets, and gaming sessions.

The bot provides a temporary voice channel system where users can automatically receive their own voice rooms, helping keep Discord servers organized.

The project also includes a ticket management system and persistent data storage using MongoDB, allowing server configurations and system data to remain available even after the bot is restarted.

Gameplay management is also being developed, allowing users to schedule gaming sessions, manage participants, and create private voice channels for each gameplay.

The project follows a modular architecture, allowing new systems and commands to be added over time.

## Current Status

The project is currently under development.

The repository is being built incrementally, with each branch representing a specific stage or feature of the development process.

### Implemented

* Initial project structure
* Discord bot configuration
* Environment variable configuration
* Command architecture
* Temporary voice channel system
* Server configuration system
* Permission management
* Ticket system
* MongoDB database integration
* Persistent server configuration

### In Development

* Command permissions

## Main Features

### Temporary Voice Channels

The bot automatically creates and manages temporary voice channels for users, providing isolated rooms while they are being used.

### Ticket System

The ticket system allows server members to create support tickets through an interactive interface.

Tickets are automatically organized using configured categories, with permissions managed to ensure that only authorized users can access each ticket.

### Gameplay Management

The gameplay management system will allow server members to organize gaming sessions directly through the bot.

Users will be able to provide:

* Game name
* Maximum number of players
* Scheduled time

The bot will publish the gameplay in a configured channel and create a private voice channel for the session.

Participants will be able to join or leave the gameplay through interactive controls. Users who join will automatically receive access to the corresponding private voice channel.

The system will also manage the player limit and gameplay lifecycle.

### Database

The bot uses MongoDB to persist important system and server configuration data.

Database storage allows configurations and information to remain available even after the bot is restarted.

## Tech Stack

| Technology     | Purpose                         |
| -------------- | ------------------------------- |
| Java 17+       | Main programming language       |
| JDA            | Discord API integration         |
| MongoDB        | Persistent data storage         |
| Maven / Gradle | Build and dependency management |

## Configuration

The bot uses environment variables for sensitive configuration data.

Create a `.env` file in the project root:

```env
DISCORD_TOKEN=your_bot_token_here
PREFIX=your_bot_prefix_here
MONGODB_URI=your_mongodb_connection_string
```

Never commit your bot token, database credentials, or other sensitive information to the repository.

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
* [x] Temporary voice channel system
* [x] Server configuration system
* [x] Permission management
* [x] Ticket system
* [x] MongoDB integration
* [x] Persistent configuration
* [x] Gameplay scheduling
* [x] Gameplay participant management
* [ ] Additional server management features

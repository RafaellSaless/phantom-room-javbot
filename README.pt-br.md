# Phantom Room JavBot

[English](README.md)

Um bot Discord em Java, limpo e modular, focado no gerenciamento de canais de voz temporários e na organização de gameplays.

> **Status do projeto:** Em desenvolvimento

## Visão Geral

O Phantom Room JavBot é um bot Discord desenvolvido em Java com o objetivo de fornecer um sistema automatizado de canais de voz temporários e funcionalidades para gerenciamento de gameplays.

O conceito principal é simples: os usuários entram em um canal de voz específico, e o bot cria automaticamente uma sala temporária para eles. Quando a sala não está mais sendo utilizada, ela pode ser removida automaticamente, mantendo o servidor organizado e reduzindo canais desnecessários.

O projeto também tem como objetivo fornecer um sistema de agendamento de gameplays, permitindo que os usuários organizem sessões de jogos, gerenciem participantes e criem automaticamente canais de voz privados para cada gameplay.

O projeto está sendo desenvolvido com uma arquitetura modular, permitindo que novos comandos e funcionalidades sejam adicionados ao longo do tempo.

## Status Atual

O projeto está atualmente em desenvolvimento.

O repositório está sendo construído de forma incremental, com cada branch representando uma etapa ou funcionalidade específica do desenvolvimento.

### Implementado

* Estrutura inicial do projeto
* Configuração básica do bot Discord
* Configuração de variáveis de ambiente
* Arquitetura de comandos
* Criação de canais de voz temporários
* Exclusão de canais de voz temporários
* Configuração de canais
* Gerenciamento de permissões

### Em Desenvolvimento

* Sistema de agendamento de gameplays

### Planejado

* Criação de gameplays através de comandos
* Gerenciamento das informações da gameplay
* Divulgação de gameplays
* Gerenciamento de participantes
* Canais de voz privados para gameplays
* Controles para entrar e sair da gameplay
* Gerenciamento do limite de jogadores
* Cancelamento de gameplays
* Gerenciamento automático dos canais de gameplay
* Nomes personalizados para canais temporários
* Controles específicos para usuários
* Comando de ajuda
* Funcionalidades adicionais de gerenciamento do servidor

## Gerenciamento de Gameplays

O sistema de gerenciamento de gameplays permitirá que os membros do servidor organizem sessões de jogos diretamente através do bot.

O usuário poderá criar uma gameplay informando:

* Nome do jogo
* Quantidade máxima de jogadores
* Horário agendado

O bot então publicará as informações da gameplay em um canal configurado especificamente para anúncios de gameplays.

Cada gameplay terá controles interativos que permitirão aos usuários entrar ou sair da sessão.

Quando um usuário entrar em uma gameplay, o bot concederá automaticamente a ele permissão para acessar o canal de voz privado criado para aquela sessão.

### Exemplo

Uma gameplay poderia ser criada com as seguintes informações:

```text
Jogo: PEAK
Jogadores: 4
Horário: 22:00
```

O bot publicará as informações da gameplay no canal configurado e criará um canal de voz privado para a sessão, por exemplo:

```text
🔒・RafaelSales — PEAK
```

Os usuários poderão entrar na gameplay através dos controles disponíveis. Assim que entrarem, receberão automaticamente acesso ao canal de voz correspondente.

O sistema também controlará a quantidade de participantes, impedindo que novos usuários entrem quando o limite máximo de jogadores for atingido.

## Tecnologias

| Tecnologia     | Finalidade                         |
| -------------- | ---------------------------------- |
| Java 17+       | Linguagem principal                |
| JDA            | Integração com a API do Discord    |
| Maven / Gradle | Gerenciamento de build e dependências |

## Configuração

O bot utiliza variáveis de ambiente para dados de configuração sensíveis.

Crie um arquivo `.env` na raiz do projeto:

```env
DISCORD_TOKEN=seu_token_do_bot_aqui
PREFIX=seu_prefixo_aqui
```

Nunca faça commit do token do bot ou de outras credenciais sensíveis no repositório.

## Intents do Discord

O bot requer os seguintes intents habilitados no Discord Developer Portal:

* `SERVER MEMBERS INTENT`
* `MESSAGE CONTENT INTENT`
* `GUILD VOICE STATES`

## Desenvolvimento

O projeto está sendo desenvolvido de forma incremental através de branches separadas do Git.

A branch `base-config` contém a configuração inicial do projeto e serve como base para o desenvolvimento das funcionalidades seguintes.

Conforme novas funcionalidades são implementadas, elas serão desenvolvidas em branches dedicadas e posteriormente integradas ao projeto principal.

## Roadmap

* [x] Configuração inicial do projeto
* [x] Arquitetura de comandos
* [x] Criação de canais de voz temporários
* [x] Agendamento de gameplays
* [x] Divulgação de gameplays
* [x] Canais privados para gameplays
* [x] Controles para entrar e sair da gameplay
* [x] Gerenciamento do limite de jogadores
* [ ] Permissão para comandos
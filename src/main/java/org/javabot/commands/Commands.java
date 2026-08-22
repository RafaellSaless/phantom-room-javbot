package org.javabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.javabot.commands.managers.CommandManager;

/**
 * Classe base abstrata que representa um comando do bot.
 * Define a estrutura básica, metadados (ID, nome e descrição) e
 * o método de execução que deve ser implementado por cada comando específico.
 */
public abstract class Commands {

    private int id;
    private String commandName;
    private String description;

    /**
     * Método abstrato responsável por executar a lógica do comando quando acionado.
     *
     * @param event O evento de mensagem recebida que disparou o comando.
     */
    public abstract void execute(MessageReceivedEvent event);


    /**
     * Define as informações básicas do comando. Geralmente chamado pelo {@link CommandManager}
     * durante o carregamento inicial.
     *
     * @param id          O ID a ser atribuído ao comando.
     * @param commandName O nome do comando.
     * @param description A descrição do comando.
     */
    public void setInfo(int id, String commandName, String description) {
        this.id = id;
        this.commandName = commandName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }
}
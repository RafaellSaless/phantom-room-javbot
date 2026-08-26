package org.javabot.Listeners;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.javabot.commands.Commands;
import org.javabot.Managers.CommandManager;


/**
 * Listener responsável por interceptar as mensagens recebidas no Discord,
 * verificar o prefixo configurado e executar os comandos correspondentes.
 * Estende o {@link ListenerAdapter} da biblioteca JDA.
 */
public class CommandListener extends ListenerAdapter {

    /** Instância do Dotenv para carregar variáveis de ambiente. */
    Dotenv dotenv = Dotenv.load();

    /** Prefixo utilizado para identificar os comandos do bot, obtido das variáveis de ambiente. */
    private final String prefix = dotenv.get("BOT_PREFIX");



    /**
     * Construtor padrão da classe.
     * Exibe uma mensagem no console informando que o listener foi instanciado.
     */
    public CommandListener() {
        System.out.println("CommandListener foi criado!");
    }


    /**
     * Método chamado automaticamente pelo JDA sempre que uma mensagem é enviada em um canal visível ao bot.
     * Filtra mensagens de bots, valida o prefixo, extrai o nome do comando e aciona sua execução.
     *
     * @param event O evento contendo os detalhes da mensagem recebida.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        if (!message.startsWith(prefix)) {
            return;
        }

        String content =
                message.substring(prefix.length()).trim();

        if (content.isEmpty()) {
            return;
        }

        String[] parts = content.split("\\s+");

        String commandName = parts[0].toLowerCase();

        Commands command =
                CommandManager.getCommand(commandName);

        if (command == null) {
            return;
        }

        command.execute(event);
    }
}
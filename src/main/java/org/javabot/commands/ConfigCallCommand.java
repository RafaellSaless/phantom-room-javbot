package org.javabot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.javabot.commands.Managers.Command;

import java.awt.*;

@Command(
        name = "configcall",
        description = "Comando para configuração do canal de criação de call"
)
public class ConfigCallCommand extends Commands { // Ou ListenerAdapter, dependendo de como gerencia seus eventos

    private static String canalId;

    @Override
    public void execute(MessageReceivedEvent event) {

        EmbedBuilder idFaltante = new EmbedBuilder();
        idFaltante.setColor(Color.red);
        idFaltante.setTitle("⚡ !configcall");
        idFaltante.setDescription("Guia de utilização do comando config");
        idFaltante.addField("Para utilizar utilize a logica:", "!configcall <ID>", true);

        EmbedBuilder idInvalido = new EmbedBuilder();
        idInvalido.setColor(Color.red);
        idInvalido.setTitle("⚡ !configcall");
        idInvalido.addField("ID invalido:", "o comando foi inserido com um id invalido", true);

        EmbedBuilder canalInvalido = new EmbedBuilder();
        canalInvalido.setColor(Color.red);
        canalInvalido.setTitle("⚡ !configcall");
        canalInvalido.addField("Canal invalido:", "O ID inserido não foi de um canal de voz", true);



        // Pega o conteúdo completo da mensagem
        String message = event.getMessage().getContentRaw();

        // Divide o texto por espaços (ex: "!config 123456" vira ["!config", "123456"])
        String[] parts = message.split("\\s+");

        // Se o usuário digitou apenas "!config" sem o ID
        if (parts.length < 2) {
            event.getChannel()
                    .sendMessageEmbeds(idFaltante.build())
                    .queue();
            return;
        }

        String arg = parts[1];
        long canalIdInput = 0;

        // Tenta converter o argumento para número
        try {
            canalIdInput = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessageEmbeds(idInvalido.build()).queue();
            return;
        }


        var channel = event.getGuild().getVoiceChannelById(canalIdInput);

        if (channel != null) {
            canalId = arg;

            // Pega o ID do servidor atual
            String guildId = event.getGuild().getId();

            // "chatvozid" or "chattextid"
            org.javabot.commands.Managers.ConfigManager.salvarConfig(guildId, "chatvozid", canalId);

            EmbedBuilder canalConfigurad = new EmbedBuilder();
            canalConfigurad.setColor(Color.green);
            canalConfigurad.setTitle("⚡ !configcall");
            canalConfigurad.addField("Canal Configurado:", "<#" + canalId + ">", true);

            event.getChannel()
                    .sendMessageEmbeds(canalConfigurad.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessageEmbeds(canalInvalido.build())
                    .queue();
        }
        canalId = "1";
    }

    public static String getCanalId() {
        return canalId;
    }
}
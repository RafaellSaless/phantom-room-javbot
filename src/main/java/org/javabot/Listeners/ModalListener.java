package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.javabot.Managers.TempVoice;
import org.javabot.Managers.TempVoiceManager;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(
            ModalInteractionEvent event
    ) {

        String id = event.getModalId();

        if (!id.startsWith("call:modal:")) {
            return;
        }

        TempVoice call =
                TempVoiceManager.getCallDoUsuario(
                        event.getUser().getIdLong()
                );

        if (call == null) {

            event.reply(
                    "❌ Você não possui uma call ativa."
            ).setEphemeral(true).queue();

            return;
        }

        switch (id) {

            case "call:modal:nome":
                alterarNome(event, call);
                break;

            case "call:modal:limite":
                alterarLimite(event, call);
                break;
        }
    }


    private void alterarNome(
            ModalInteractionEvent event,
            TempVoice call
    ) {

        String novoNome =
                event.getValue("nome")
                        .getAsString();

        TempVoiceManager.alterarNome(
                event.getJDA(),
                call,
                novoNome
        );

        event.reply(
                "✅ Nome da call alterado para **"
                        + novoNome
                        + "**."
        ).queue();
    }


    private void alterarLimite(
            ModalInteractionEvent event,
            TempVoice call
    ) {

        String valor =
                event.getValue("limite")
                        .getAsString();

        int limite;

        try {

            limite = Integer.parseInt(valor);

        } catch (NumberFormatException e) {

            event.reply(
                    "❌ Digite um número válido."
            ).setEphemeral(true).queue();

            return;
        }

        if (limite < 0 || limite > 99) {

            event.reply(
                    "❌ O limite deve estar entre 0 e 99."
            ).setEphemeral(true).queue();

            return;
        }

        TempVoiceManager.alterarLimite(
                event.getJDA(),
                call,
                limite
        );

        event.reply(
                "✅ Limite alterado para **"
                        + limite
                        + "** usuários."
        ).queue();
    }
}
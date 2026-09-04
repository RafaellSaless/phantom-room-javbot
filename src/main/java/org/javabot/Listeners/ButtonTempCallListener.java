package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.javabot.Managers.TempVoice;
import org.javabot.Managers.TempVoiceManager;

public class ButtonTempCallListener {
    public void handle(ButtonInteractionEvent event, String id) {
        TempVoice call =
                TempVoiceManager.getCallDoUsuario(
                        event.getUser().getIdLong()
                );

        if (call == null) {

            event.reply(
                    "❌ Você não possui uma call temporária ativa."
            ).setEphemeral(true).queue();

            return;
        }

        switch (id) {

            case "call:nome":
                abrirModalNome(event);
                break;

            case "call:limite":
                abrirModalLimite(event);
                break;

            case "call:privacidade":
                configurarPrivacidade(event);
                break;

            case "call:excluir":
                excluirCall(event, call);
                break;
        }
    }


    /**
     * Cria o modal para mudar nome de uma call
     * @param event evento da interação
     */
    private void abrirModalNome(
            ButtonInteractionEvent event
    ) {

        event.replyModal(
                net.dv8tion.jda.api.interactions.modals.Modal
                        .create(
                                "call:modal:nome",
                                "Alterar nome da Call"
                        )
                        .addActionRow(
                                net.dv8tion.jda.api.interactions.components.text.TextInput
                                        .create(
                                                "nome",
                                                "Novo nome",
                                                net.dv8tion.jda.api.interactions.components.text.TextInputStyle.SHORT
                                        )
                                        .setRequired(true)
                                        .setMinLength(1)
                                        .setMaxLength(50)
                                        .build()
                        )
                        .build()
        ).queue();
    }


    private void abrirModalLimite(
            ButtonInteractionEvent event
    ) {

        event.replyModal(
                net.dv8tion.jda.api.interactions.modals.Modal
                        .create(
                                "call:modal:limite",
                                "Alterar limite da Call"
                        )
                        .addActionRow(
                                net.dv8tion.jda.api.interactions.components.text.TextInput
                                        .create(
                                                "limite",
                                                "Limite de usuários",
                                                net.dv8tion.jda.api.interactions.components.text.TextInputStyle.SHORT
                                        )
                                        .setPlaceholder(
                                                "Ex: 5"
                                        )
                                        .setRequired(true)
                                        .build()
                        )
                        .build()
        ).queue();
    }

    /**
     * Cria o modal para mudar a privacidade
     * @param event evento da interação
     */
    private void configurarPrivacidade(
            ButtonInteractionEvent event
    ) {

        TempVoice call =
                TempVoiceManager.getCallDoUsuario(
                        event.getUser().getIdLong()
                );

        if (call == null) {

            event.reply(
                    "❌ Você não possui uma call ativa."
            ).queue();

            return;
        }

        TempVoiceManager.alternarPrivacidade(
                event.getJDA(),
                call
        );

        event.reply(
                "🔒 A privacidade da sua call foi alterada."
        ).queue();
    }


    private void excluirCall(
            ButtonInteractionEvent event,
            TempVoice call
    ) {

        TempVoiceManager.excluirCall(
                event.getJDA(),
                call
        );

        event.reply(
                "🗑️ Sua call foi excluída."
        ).queue();
    }


}




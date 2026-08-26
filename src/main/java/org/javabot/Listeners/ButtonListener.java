package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.javabot.Managers.TempVoiceManager;
import org.javabot.Managers.TempVoice;

/**
 * Listener que vai "ouvir" as ações do usuario por butões, utilizando atualmente em DM's
 * Estende o {@link ListenerAdapter} da biblioteca JDA.
 */
public class ButtonListener extends ListenerAdapter {

    /**
     * Função para identifcar a intereção com os botões da DM
     * @param event evento a ter interação
     */
    @Override
    public void onButtonInteraction(
            ButtonInteractionEvent event
    ) {

        String id = event.getComponentId();

        // Só processa nossos botões
        if (id.startsWith("call:")) {
            processarBotoesCallTemp(event, id);
        }
        if (id.startsWith("game:")) {
            processarBotoesGameSchedule(event, id);
        }

    }

    private void processarBotoesGameSchedule(ButtonInteractionEvent event, String id) {


        if (id.equals("game:marcar")) {

            Modal modal = Modal.create(
                            "game:schedule",
                            "Marcar jogo"
                    )

                    .addActionRow(
                            TextInput.create(
                                            "jogo",
                                            "Nome do jogo",
                                            TextInputStyle.SHORT
                                    )
                                    .setPlaceholder(
                                            "Ex: PEAK"
                                    )
                                    .setRequired(true)
                                    .setMinLength(1)
                                    .setMaxLength(100)
                                    .build()
                    )

                    .addActionRow(
                            TextInput.create(
                                            "horario",
                                            "Horário",
                                            TextInputStyle.SHORT
                                    )
                                    .setPlaceholder(
                                            "Ex: 20:30"
                                    )
                                    .setRequired(true)
                                    .setMaxLength(10)
                                    .build()
                    )

                    .addActionRow(
                            TextInput.create(
                                            "max",
                                            "Máximo de participantes",
                                            TextInputStyle.SHORT
                                    )
                                    .setPlaceholder(
                                            "Ex: 5"
                                    )
                                    .setRequired(true)
                                    .setMaxLength(3)
                                    .build()
                    )

                    .build();

            event.replyModal(modal).queue();

            return;
        }
    }

    private void processarBotoesCallTemp(ButtonInteractionEvent event, String id) {
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
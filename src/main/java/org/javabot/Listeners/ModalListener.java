package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.javabot.Managers.TempVoice;
import org.javabot.Managers.TempVoiceManager;
import org.javabot.Managers.GameManager;

/**
 * Listener responsavel por intermediar ações dos botões
 * Estende o {@link ListenerAdapter} da biblioteca JDA.
 */
public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(
            ModalInteractionEvent event
    ) {

        String id = event.getModalId();

        if (id.startsWith("call:modal:")) {
            processarCall(event, id);
        }

        if(id.startsWith("game:")) {
            processarSchedule(event, id);
        }

    }


    private void processarSchedule(ModalInteractionEvent event, String id) {

        if (id.equals("game:schedule")) {

            long userId =
                    event.getUser().getIdLong();

            String guildId = GameManager.getGuildId(userId);

            if (guildId == null) {

                event.reply(
                                "❌ Não encontrei um agendamento sendo configurado."
                        )
                        .setEphemeral(true)
                        .queue();

                return;
            }


            String jogo =
                    event.getValue("jogo")
                            .getAsString()
                            .trim();


            String horario =
                    event.getValue("horario")
                            .getAsString()
                            .trim();


            String maxParticipantes =
                    event.getValue("max")
                            .getAsString()
                            .trim();

            String horarioFormatado = validarHorario(horario.split(":"), event);

            if( horarioFormatado == null) {
                return;
            }

            int participantesValidado = validarParticipantes(maxParticipantes, event);
            if(participantesValidado == -1) {
                return;
            }



            /*
             * Publica o agendamento.
             */
            GameManager.publicarAgendamento(
                    event.getJDA(),
                    userId,
                    guildId,
                    jogo,
                    horarioFormatado,
                    participantesValidado
            );


            /*
             * Remove o processo temporário.
             */
            GameManager.removerConfiguracao(
                    userId
            );

            event.reply(
                            "✅ **Agendamento criado com sucesso!**\n\n" +
                                    "🎮 Jogo: `" + jogo + "`\n" +
                                    "🕐 Horário: `" + horario + "`\n" +
                                    "👥 Máximo: `" + maxParticipantes + "`"
                    )
                    .setEphemeral(true)
                    .queue();
            return;

    }
    }


    private void processarCall(ModalInteractionEvent event, String id) {


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

    private int validarParticipantes(
            String participantesString,
            ModalInteractionEvent event) {


        int maxParticipantes=-1;

        try {

            maxParticipantes =
                    Integer.parseInt(
                            participantesString
                    );
        } catch (NumberFormatException e) {

            event.reply(
                            "❌ O máximo de participantes precisa ser um número."
                    )
                    .setEphemeral(true)
                    .queue();
            return -1;
        }

        if (maxParticipantes > 20) {

            event.reply(
                            "❌ O máximo de participantes precisa ser menor ou igual a 20."
                    )
                    .setEphemeral(true)
                    .queue();
            return -1;

        }

        if (maxParticipantes <= 0) {

            event.reply(
                            "❌ O máximo de participantes precisa ser maior que 0."
                    )
                    .setEphemeral(true)
                    .queue();
            return -1;

        }

        return maxParticipantes;

    }

    private String validarHorario(
            String[] horario,
            ModalInteractionEvent event) {



        if (horario.length == 2) {
            try {

                int hora = Integer.parseInt(horario[0]);
                int minuto = Integer.parseInt(horario[1]);

                if (!((hora > 23 || hora < 0) && (minuto > 59 || minuto < 0))) {

                    // Supondo que 'hora' e 'minuto' passaram na validação
                    return String.format("%02d:%02d", hora, minuto);
                } else {
                    event.reply(
                                    "❌ Horário invalido"
                            )
                            .setEphemeral(true)
                            .queue();
                }

            } catch (NumberFormatException e) {
                event.reply(
                                "❌ Horário invalido"
                        )
                        .setEphemeral(true)
                        .queue();
            }
        }
        return null;
    }

}
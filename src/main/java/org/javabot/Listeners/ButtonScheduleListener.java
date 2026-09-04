package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.javabot.Managers.GameManager;
import org.javabot.Managers.GameSchedule;

public class ButtonScheduleListener {

    public void handle(ButtonInteractionEvent event, String id) {

        if (id.startsWith("game:fechar:")) {
            String scheduleId = id.replace("game:fechar:", "");

            GameSchedule schedule =
                    GameManager.getAgendamento( event.getGuild().getId(), scheduleId );


            long userIdClick = event.getUser().getIdLong();

            if(schedule == null) {
                event.reply("❌ Este agendamento não foi encontrado ou já foi fechado.").setEphemeral(true).queue();
                return;
            }

            if(!schedule.ehDono(userIdClick)) {
                event.reply("❌ Apenas o criador deste agendamento pode fechá-lo!").setEphemeral(true).queue();
                return;
            }

            GameManager.deletarCanais(event, schedule);

        }


        if (id.startsWith("game:participar:")) {
            String scheduleId = id.replace("game:participar:", "");

            long userId = event.getUser().getIdLong();

            GameManager.adicionarPlayerAgendamento(
                    event.getGuild().getId(),
                    scheduleId,
                    userId,
                    event
            );
        }

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

        if (id.startsWith("game:sair:")) {

            String scheduleId =
                    id.replace("game:sair:", "");

            long userId =
                    event.getUser().getIdLong();

            GameManager.removerPlayerAgendamento(
                    event.getGuild().getId(),
                    scheduleId,
                    userId,
                    event
            );

            return;
        }
    }

}

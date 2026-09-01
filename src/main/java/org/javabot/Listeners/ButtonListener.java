package org.javabot.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bson.Document;
import org.javabot.Managers.GameManager;
import org.javabot.Managers.GameSchedule;
import org.javabot.Managers.TempVoiceManager;
import org.javabot.Managers.TempVoice;
import org.javabot.repository.TicketRepository;

import java.awt.*;

/**
 * Listener que vai "ouvir" as ações do usuario por butões, utilizando atualmente em DM's
 * Estende o {@link ListenerAdapter} da biblioteca JDA.
 */
public class ButtonListener extends ListenerAdapter {
    private final TicketRepository repository = new TicketRepository();
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
        if (id.startsWith("ticket:")) {
            processarBotoesTicket(event, id);
        }

    }

    private void processarBotoesGameSchedule(ButtonInteractionEvent event, String id) {

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

    private void processarBotoesTicket(ButtonInteractionEvent event, String id) {

        if(id.equalsIgnoreCase("ticket:create")) {



            String guildId = event.getGuild().getId();
            Document config = repository.getTicketConfig(guildId);

            if (config == null) {
                event.reply("❌ O sistema de tickets ainda não foi configurado neste servidor.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            String categoryId = config.getString("categoryId");
            String roleId = config.getString("roleId");

            Category category = event.getGuild().getCategoryById(categoryId);
            Role supportRole = event.getGuild().getRoleById(roleId);

            if (category == null) {
                event.reply("❌ A categoria configurada para os tickets não existe mais.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            if (supportRole == null) {
                event.reply("❌ O cargo configurado para atendimento não existe mais.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            Long ticketNumber = repository.reserveNextTicketNumber(guildId);

            if (ticketNumber == null) {
                event.reply("❌ Não foi possível reservar um número para o ticket.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            String ticketName = String.format("ticket-%03d", ticketNumber);

            event.getGuild()
                    .createTextChannel(ticketName, category)
                    .queue(channel -> TicketConfigurarPermissoes(
                                    event,
                                    channel,
                                    supportRole
                            ), failure ->
                                    event.reply("❌ Não consegui criar o ticket. Verifique as permissões do bot.")
                                            .setEphemeral(true)
                                            .queue()
                    );

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


    private void TicketConfigurarPermissoes(
            ButtonInteractionEvent event,
            TextChannel channel,
            Role supportRole
    ) {

        String serverName = event.getGuild().getName();

        Member member = event.getMember();

        // Ninguém vê o ticket por padrão.
        channel.upsertPermissionOverride(event.getGuild().getPublicRole())
                .deny(Permission.VIEW_CHANNEL)
                .queue();

        // O usuário que abriu o ticket pode visualizar e escrever.
        channel.upsertPermissionOverride(member)
                .grant(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                )
                .queue();

        // O cargo de atendimento pode visualizar e responder.
        channel.upsertPermissionOverride(supportRole)
                .grant(
                        Permission.VIEW_CHANNEL,
                        Permission.MESSAGE_SEND,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES
                )
                .queue();

        event.reply("✅ Seu ticket foi criado: " + channel.getAsMention())
                .setEphemeral(true)
                .queue();

        EmbedBuilder ticketsucess = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("🎫 Sistema de Tickets - " + serverName)
                .setDescription("Bem vindo ao sistema de ticket!" + channel.getName())
                .addField(" - ",  "um dos nossos " + supportRole.getAsMention() + " poderá responder aqui!", false)
                .addField(" - ", "Seja bem vindo " + member.getAsMention(), false);

        String bannerUrlPlayer = member.getAvatarUrl();
        String bannerUrlGuild = event.getGuild().getBannerUrl();


        if (bannerUrlPlayer != null) {
            ticketsucess.setImage(bannerUrlPlayer);
        } else if(bannerUrlGuild != null) {
            ticketsucess.setImage(bannerUrlGuild);
        }else {
            String botAvatarUrl = event.getJDA().getSelfUser().getEffectiveAvatarUrl();
            ticketsucess.setImage(botAvatarUrl);
        }

        channel.sendMessageEmbeds(ticketsucess.build()).queue();
        channel.sendMessage(supportRole.getAsMention()).queue();
    }
}



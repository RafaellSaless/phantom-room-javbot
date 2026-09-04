package org.javabot.Listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listener que vai "ouvir" as ações do usuario por butões
 * Estende o {@link ListenerAdapter} da biblioteca JDA.
 */
public class ButtonListenerController extends ListenerAdapter {
    public final ButtonTicketListener tickerListener;
    public final ButtonScheduleListener scheduleListener;
    public final ButtonTempCallListener tempCallListener;

    public ButtonListenerController() {
        this.tempCallListener = new ButtonTempCallListener();
        this.scheduleListener = new ButtonScheduleListener();
        this.tickerListener = new ButtonTicketListener();
    }

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
            tempCallListener.handle(event, id);
        }
        if (id.startsWith("game:")) {
            scheduleListener.handle(event, id);
        }
        if (id.startsWith("ticket:")) {
            tickerListener.handle(event, id);
        }

    }

}



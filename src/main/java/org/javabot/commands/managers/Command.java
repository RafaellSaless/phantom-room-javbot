package org.javabot.commands.managers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Anotação utilizada para marcar classes como comandos do bot.
 * Permite definir metadados essenciais como o nome e a descrição do comando,
 * que serão lidos dinamicamente pelo {@link CommandManager} em tempo de execução.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();
    String description();

}
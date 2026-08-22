package org.javabot.commands.Managers;

import org.javabot.commands.Commands;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Gerenciador responsável por carregar, armazenar e disponibilizar os comandos do bot.
 * Utiliza a biblioteca Reflections para escanear o pacote de comandos em busca
 * de classes anotadas com {@link Command}.
 *
 */
public class CommandManager {

    /** Próximo ID a ser atribuído a um comando carregado. */
    private static int nextId = 1;

    /** Mapa que armazena os comandos registrados, mapeando o nome do comando para sua instância. */
    private static final Map<String, Commands> commands = new HashMap<>();


    /**
     * Escaneia o pacote {@code org.javabot.commands} em busca de classes anotadas com {@link Command},
     * instancia cada comando encontrado, define suas informações básicas (ID, nome e descrição)
     * e os armazena no registro interno.
     *
     * Caso ocorra algum erro durante a instanciação reflexiva de uma classe, uma exceção
     * será tratada e o erro impresso no console.
     */
    public static void loadCommands() {

        Reflections reflections =
                new Reflections("org.javabot.commands");

        Set<Class<?>> classes =
                reflections.getTypesAnnotatedWith(Command.class);

        for (Class<?> clazz : classes) {

            try {

                Command annotation =
                        clazz.getAnnotation(Command.class);

                String name = annotation.name();
                String description = annotation.description();

                Commands command =
                        (Commands) clazz.getDeclaredConstructor().newInstance();

                command.setInfo(
                        nextId++,
                        name,
                        description
                );

                commands.put(name, command);

                System.out.println(
                        "Comando carregado: " + name
                );

            } catch (Exception e) {

                System.out.println(
                        "Erro ao carregar: " +
                                clazz.getSimpleName()
                );

                e.printStackTrace();
            }
        }
    }

    /**
     * Busca um comando registrado pelo seu nome.
     *
     * @param name O nome do comando a ser buscado.
     * @return A instância de {@link Commands} correspondente, ou {@code null} se o comando não for encontrado.
     */
    public static Commands getCommand(String name) {
        return commands.get(name);
    }

    /**
     * Retorna o mapa contendo todos os comandos carregados.
     *
     * @return Um {@link Map} onde a chave é o nome do comando e o valor é a instância de {@link Commands}.
     */
    public static Map<String, Commands> getCommands() {
        return commands;
    }
}

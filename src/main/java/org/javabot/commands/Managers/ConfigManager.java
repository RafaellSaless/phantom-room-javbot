package org.javabot.commands.Managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final String FILE_PATH = "config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Carrega todas as configurações do JSON para um Map (GuildId -> CanalId)
    public static Map<String, String> carregarConfigs() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>(); // Retorna um mapa vazio se o arquivo não existir
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> configs = gson.fromJson(reader, type);
            return configs != null ? configs : new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    // Salva ou atualiza a configuração de um servidor específico no mapa e grava no JSON
    public static void salvarConfig(String guildId, String canalId) {
        Map<String, String> configs = carregarConfigs();

        // Adiciona ou substitui o canal do servidor correspondente
        configs.put(guildId, canalId);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(configs, writer);
            System.out.println("Configuração salva com sucesso para a guilda: " + guildId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pega o canal configurado de uma guilda específica rapidamente
    public static String getCanalDoServidor(String guildId) {
        Map<String, String> configs = carregarConfigs();
        return configs.get(guildId);
    }
}
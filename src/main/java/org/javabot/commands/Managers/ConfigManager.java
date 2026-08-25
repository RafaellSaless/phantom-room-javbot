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

    // Carrega todas as configurações: GuildId -> (ChaveConfig -> ValorId)
    public static Map<String, Map<String, String>> carregarConfigs() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
            Map<String, Map<String, String>> configs = gson.fromJson(reader, type);
            return configs != null ? configs : new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Salva uma configuração específica (ex: chatvozid ou chattextid) para a guilda
    public static void salvarConfig(String guildId, String tipoCanal, String canalId) {
        Map<String, Map<String, String>> configs = carregarConfigs();

        // Pega o mapa da guilda ou cria um novo se não existir
        Map<String, String> dadosGuilda = configs.getOrDefault(guildId, new HashMap<>());

        // Adiciona ou atualiza o tipo específico (ex: "chatvozid" = "1293139")
        dadosGuilda.put(tipoCanal, canalId);

        // Atualiza no mapa principal
        configs.put(guildId, dadosGuilda);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(configs, writer);
            System.out.println("Configuração salva com sucesso para a guilda: " + guildId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pega um ID específico de uma guilda (ex: getCanalDoServidor(guildId, "chatvozid"))
    public static String getCanalDoServidor(String guildId, String tipoCanal) {
        Map<String, Map<String, String>> configs = carregarConfigs();
        Map<String, String> dadosGuilda = configs.get(guildId);

        if (dadosGuilda != null) {
            return dadosGuilda.get(tipoCanal);
        }
        return null;
    }
}
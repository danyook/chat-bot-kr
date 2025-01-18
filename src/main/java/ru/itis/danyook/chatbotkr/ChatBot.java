package ru.itis.danyook.chatbotkr;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ChatBot {
    private static final String WEATHER_API_KEY = "ec6ae61f58c44f36d3bd7d4f99c9993a";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    private static final String EXCHANGE_API_KEY = "02c622f203fe44bbb17a3c09fa686187";
    private static final String EXCHANGE_API_URL = "https://openexchangerates.org/api/latest.json?app_id=%25s&symbols=USD,RUB";

    private final Map<String, String> commands;

    public ChatBot() {
        commands = new HashMap<>();
        commands.put("list", "Список доступных команд: list, weather [город], exchange [валюта], quit");
    }

    public String handleCommand(String command) {
        String[] words = command.split("\\s+");
        String mainCommand = words[0].toLowerCase();

        switch (mainCommand) {
            case "list":
                return commands.get("list");

            case "weather":
                if (words.length < 2) {
                    return "Укажите город после команды 'weather' город через пробел";
                }
                return getWeather(words[1]);

            case "exchange":
                if (words.length < 2) {
                    return "Укажите валюту после команды 'exchange' валюту через пробел";
                }
                return getExchangeRate(words[1]);

            case "quit":
                return commands.get("quit");

            default:
                return "Неизвестная команда. Введите 'list' для получения списка команд.";
        }
    }

    private String getWeather(String city) {
        try {
            String url = WEATHER_API_URL.formatted(city, WEATHER_API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            System.out.println("Requesting weather data from: " + url);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = json.getJSONObject("main").getDouble("temp");

            return "Погода в %s: %s, температура: %.2f°C".formatted(city, description, temp);
        } catch (Exception e) {
            return "Не удалось получить погоду. Проверьте город.";
        }
    }

    private String getExchangeRate(String currency) {
        try {
            String url = EXCHANGE_API_URL.formatted(EXCHANGE_API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Не удалось получить курс валюты. Проверьте код валюты.";
            }

            JSONObject json = new JSONObject(response.body());

            if (!json.getJSONObject("rates").has(currency.toUpperCase())) {
                return "Код валюты не найден. Проверьте код валюты.";
            }

            double rate = json.getJSONObject("rates").getDouble(currency.toUpperCase());

            double rubToUsdRate = json.getJSONObject("rates").getDouble("RUB");
            double currencyRateToRub = rubToUsdRate / rate;

            return "Курс %s к рублю: %.2f".formatted(currency.toUpperCase(), currencyRateToRub);
        } catch (Exception e) {
            return "Не удалось получить курс валюты. Проверьте код валюты.";
        }
    }

}


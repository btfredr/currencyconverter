import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.control.Label;
import java.net.URI;
import java.util.*;

public class CurrencyConverter extends Application {
    private final String API_KEY = "cur_live_oA4z9KL3fzjjCuiSA1EzZT38Bc5ioiW13A2k2lo8"; // Api key
    private final String BASE_CURRENCY = "NOK"; // Standard currency used for conversion
    private Map<String, Double> currencyRates = new HashMap<>(); // Saving rates

    @Override
    public void start(Stage stage) {
        // UI components
        TextField amount = new TextField();
        amount.setPromptText("Enter amount"); // Placeholder when empty

        // ComboBox to choose "from" currency
        ComboBox<String> fromCurrency = new ComboBox<>();
        fromCurrency.setPromptText("Choose currency"); // Placeholder text when field is empty

        // ComboBox to choose "to" currency
        ComboBox<String> toCurrency = new ComboBox<>();
        toCurrency.setPromptText("Choose currency");

        Button calculate = new Button("Calculate");

        Label result = new Label("Result: ");

        // VBox to create user interface with elements vertically aligned with 10px space
        VBox root = new VBox(12.5, new Label("Please choose a currency to convert from: "), fromCurrency, amount, new Label("Please choose a currency to convert to: "), toCurrency, calculate, result);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 400);

        stage.setScene(scene);
        stage.setTitle("Currency converter");
        stage.show();

        getCurrencyRates(fromCurrency, toCurrency);

        calculate.setOnAction(e -> {
            String from = fromCurrency.getValue();
            String to = toCurrency.getValue();
            String amountStr = amount.getText();

            if (from == null || to == null || amountStr.isEmpty()) {
                result.setText("Select currencies and enter an amount.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr.replace(",", "."));
                double rateFrom = currencyRates.getOrDefault(from, 1.0);
                double rateTo = currencyRates.getOrDefault(to, 1.0);
                double converted = amount * (rateTo / rateFrom);
                result.setText(String.format("Result: %.2f %s = %.2f %s", amount, from, converted, to));
            } catch (NumberFormatException ex) {
                result.setText("Unvalid amount");
            }
        });
    }

    private void getCurrencyRates(ComboBox<String> fromCurrency, ComboBox<String> toCurrency) {
        // Creating a task to run the API call in the background
        Task<Map<String, Double>> task = new Task<>() {
        @Override
        protected Map<String, Double> call() throws Exception {
            // Creating a HTTP client to send a request
            HttpClient client = HttpClient.newHttpClient();

            // API URL with key and base currency
            String url = String.format("https://api.currencyapi.com/v3/latest?apikey=%s&base_currency=%s",
                    API_KEY, BASE_CURRENCY);

            // GET request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            // Sending the request and recieving a response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check HTTP status code
            if (response.statusCode() != 200) {
                throw new RuntimeException("API error: HTTP "+ response.statusCode());
            }

            // Parse JSON data with Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode data = root.path("data");

            // Building map with currency rates
            Map<String, Double> rates = new HashMap<>();
            for (JsonNode node : data) {
                String currency = node.path("code").asText();
                double rate = node.path("value").asDouble();
                rates.put(currency, rate);
            }
            return rates;
        }};

        // Handle successful response
        task.setOnSucceeded(e -> {
            currencyRates = task.getValue();
            List<String> currencies = new ArrayList<>(currencyRates.keySet());
            Collections.sort(currencies);
            fromCurrency.setItems(FXCollections.observableArrayList(currencies));
            toCurrency.setItems(FXCollections.observableArrayList(currencies));
            fromCurrency.setValue("NOK");
            toCurrency.setValue("USD");
        });

        // Handle unsuccessful rseponse
        task.setOnFailed(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error while fetching rates. " + task.getException().getMessage());
            alert.show();
        });

        new Thread(task).start();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
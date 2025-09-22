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



    }
}
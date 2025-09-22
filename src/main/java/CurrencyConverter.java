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
    private String apiUrl = "https://api.currencyapi.com/v3/latest?apikey=cur_live_oA4z9KL3fzjjCuiSA1EzZT38Bc5ioiW13A2k2lo8";

    @Override
    public void start(Stage stage) {
        Label title = new Label("Please select currency to convert");
    }
}
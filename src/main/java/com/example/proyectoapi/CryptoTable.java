package com.example.proyectoapi;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CryptoTable extends Application {

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/list";

    private TableView<Crypto> table;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        table = new TableView<>();
        table.getColumns().add(createColumn("Name", "name"));
        table.getColumns().add(createColumn("Symbol", "symbol"));

        StackPane root = new StackPane(table);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println(body); // for debugging purposes

                // Parse the JSON response and populate the table
                ObservableList<Crypto> data = FXCollections.observableArrayList();
                JSONArray jsonArray = new JSONArray(body);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    Crypto crypto = new Crypto(json.getString("name"), json.getString("symbol"));
                    data.add(crypto);
                }
                table.setItems(data);
            }
        });
    }

    private <S, T> TableColumn<S, T> createColumn(String title, String propertyName) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return col;
    }

    public static class Crypto {
        private String name;
        private String symbol;

        public Crypto(String name, String symbol) {
            this.name = name;
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}


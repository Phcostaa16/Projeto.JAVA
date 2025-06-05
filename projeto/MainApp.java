package com.example.projeto;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        Button btnAdicionar = new Button("Nova Transferência");
        Button btnListar = new Button("Consultar Transferências");

        btnAdicionar.setOnAction(e -> new TransferenciaForm().show());
        btnListar.setOnAction(e -> new TransferenciaListScreen().show());

        VBox layout = new VBox(20, btnAdicionar, btnListar);
        Scene scene = new Scene(layout, 400, 300);

        stage.setTitle("Mercado de Transferências");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

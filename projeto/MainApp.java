package com.example.projeto;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        Button btnAdicionarJogador = new Button("Registrar Jogador");
        Button btnAdicionarTransferencia = new Button("Nova Transferência");
        Button btnListarTransferencias = new Button("Consultar Transferências");

        btnAdicionarJogador.setOnAction(e -> new PlayerRegistrationForm().show());
        btnAdicionarTransferencia.setOnAction(e -> new TransferenciaForm().show());
        btnListarTransferencias.setOnAction(e -> new TransferenciaListScreen().show());

        VBox layout = new VBox(20, btnAdicionarJogador, btnAdicionarTransferencia, btnListarTransferencias);
        layout.setAlignment(Pos.CENTER);
        layout.setMinWidth(400);
        layout.setMinHeight(300);

        Scene scene = new Scene(layout, 400, 300);

        stage.setTitle("Mercado de Transferências");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

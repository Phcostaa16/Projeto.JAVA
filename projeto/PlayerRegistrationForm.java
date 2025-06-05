package com.example.projeto;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayerRegistrationForm {
    public void show() {
        Stage stage = new Stage();

        Label lblNome = new Label("Nome do Jogador:");
        TextField txtPlayerName = new TextField();
        Button btnSave = new Button("Salvar Jogador");

        btnSave.setOnAction(e -> {
            String playerName = txtPlayerName.getText().trim();
            if (!playerName.isEmpty()) {
                try {
                    List<Player> players = PlayerDAO.load();
                    players.add(new Player(playerName));
                    PlayerDAO.save(players);
                    showSuccess("Jogador salvo com sucesso!");
                    txtPlayerName.clear();
                } catch (IOException ex) {
                    showAlert("Erro ao salvar jogador: " + ex.getMessage());
                }
            } else {
                showAlert("O nome do jogador não pode estar vazio.");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, lblNome, txtPlayerName);
        grid.add(btnSave, 1, 1);

        VBox root = new VBox(15, grid);
        root.setAlignment(Pos.CENTER);
        root.setMinWidth(350);
        root.setMinHeight(150);

        stage.setScene(new Scene(root));
        stage.setTitle("Registrar Jogador");
        stage.centerOnScreen();
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


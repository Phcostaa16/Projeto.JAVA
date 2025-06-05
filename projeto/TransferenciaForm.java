package com.example.projeto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TransferenciaForm {
    private MercadoDeTransferencia transferencia;
    private ObservableList<String> jogadoresObservable;

    public TransferenciaForm(MercadoDeTransferencia transferencia) {
        this.transferencia = transferencia;
    }

    public TransferenciaForm() {
        this(null);
    }

    public void show() {
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Load players for suggestions
        List<Player> jogadores = PlayerDAO.load();
        jogadoresObservable = FXCollections.observableArrayList();
        for (Player p : jogadores) {
            jogadoresObservable.add(p.getName());
        }

        // Use ComboBox to have autocomplete and suggestions
        ComboBox<String> cbJogador = new ComboBox<>(jogadoresObservable);
        cbJogador.setEditable(true);

        TextField txtOrigem = new TextField();
        TextField txtDestino = new TextField();
        TextField txtValor = new TextField();
        DatePicker dtData = new DatePicker();
        ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll(MercadoDeTransferencia.StatusTransferencia.values());

        // Preencher campos se editar
        if (transferencia != null) {
            preencherCampos(cbJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus);
        }

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> salvarTransferencia(cbJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus, stage));

        grid.addRow(0, new Label("Jogador:"), cbJogador);
        grid.addRow(1, new Label("Clube Origem:"), txtOrigem);
        grid.addRow(2, new Label("Clube Destino:"), txtDestino);
        grid.addRow(3, new Label("Valor (milhões €):"), txtValor);
        grid.addRow(4, new Label("Data:"), dtData);
        grid.addRow(5, new Label("Status:"), cbStatus);
        grid.add(btnSalvar, 1, 6);

        VBox root = new VBox(15, grid);
        root.setAlignment(Pos.CENTER);
        root.setMinWidth(400);
        root.setMinHeight(300);

        stage.setScene(new Scene(root));
        stage.setTitle(transferencia != null ? "Editar Transferência" : "Nova Transferência");
        stage.centerOnScreen();
        stage.show();
    }

    private void preencherCampos(ComboBox<String> cbJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus) {
        cbJogador.setValue(transferencia.getJogador());
        txtOrigem.setText(transferencia.getClubeOrigem());
        txtDestino.setText(transferencia.getClubeDestino());
        txtValor.setText(String.valueOf(transferencia.getValor()));
        dtData.setValue(transferencia.getDataTransferencia());
        cbStatus.setValue(transferencia.getStatus());
    }

    private void salvarTransferencia(ComboBox<String> cbJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus, Stage stage) {
        try {
            String jogadorNome = cbJogador.getEditor().getText().trim();

            // Validação se jogador existe na lista
            if (!jogadoresObservable.contains(jogadorNome)) {
                mostrarAlerta("Jogador não cadastrado! Cadastre o jogador antes.");
                return;
            }

            MercadoDeTransferencia novaTransferencia = criarNovaTransferencia(jogadorNome, txtOrigem, txtDestino, txtValor, dtData, cbStatus);
            if (novaTransferencia.validarTransferencia()) {
                List<MercadoDeTransferencia> transferencias = TransferenciaDAO.carregar();
                atualizarTransferencias(novaTransferencia, transferencias);
                TransferenciaDAO.salvar(transferencias);
                mostrarSucesso("Transferência salva com sucesso!");
                stage.close();
            } else {
                mostrarAlerta("Dados inválidos! Verifique se os clubes são diferentes e o valor é positivo.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Valor inválido! Use números (ex: 50.5)");
        } catch (IOException ex) {
            mostrarAlerta("Erro ao salvar os dados!");
        }
    }

    private MercadoDeTransferencia criarNovaTransferencia(String jogadorNome, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus) {
        int id = transferencia != null ? transferencia.getId() : 0;
        MercadoDeTransferencia novaTransferencia = new MercadoDeTransferencia(
                id,
                jogadorNome,
                txtOrigem.getText(),
                txtDestino.getText(),
                Double.parseDouble(txtValor.getText()),
                dtData.getValue()
        );
        novaTransferencia.setStatus(cbStatus.getValue());
        return novaTransferencia;
    }

    private void atualizarTransferencias(MercadoDeTransferencia novaTransferencia, List<MercadoDeTransferencia> transferencias) {
        if (transferencia != null) {
            int index = transferencias.indexOf(transferencia);
            if (index != -1) {
                transferencias.set(index, novaTransferencia);
            }
        } else {
            transferencias.add(novaTransferencia);
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

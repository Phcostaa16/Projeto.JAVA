package com.example.projeto;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class TransferenciaForm {
    private MercadoDeTransferencia transferencia;

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

        // Campos do formulário
        TextField txtJogador = new TextField();
        TextField txtOrigem = new TextField();
        TextField txtDestino = new TextField();
        TextField txtValor = new TextField();
        DatePicker dtData = new DatePicker();
        ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll(MercadoDeTransferencia.StatusTransferencia.values());

        // Preenchendo campos se estiver editando
        if (transferencia != null) {
            preencherCampos(txtJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus);
        }

        // Botão de salvar
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> salvarTransferencia(txtJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus, stage));

        // Adiciona componentes ao grid
        adicionarComponentesAoGrid(grid, txtJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus, btnSalvar);

        stage.setScene(new Scene(grid, 400, 300));
        stage.setTitle(transferencia != null ? "Editar Transferência" : "Nova Transferência");
        stage.show();
    }

    private void preencherCampos(TextField txtJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus) {
        txtJogador.setText(transferencia.getJogador());
        txtOrigem.setText(transferencia.getClubeOrigem());
        txtDestino.setText(transferencia.getClubeDestino());
        txtValor.setText(String.valueOf(transferencia.getValor()));
        dtData.setValue(transferencia.getDataTransferencia());
        cbStatus.setValue(transferencia.getStatus());
    }

    private void salvarTransferencia(TextField txtJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus, Stage stage) {
        try {
            MercadoDeTransferencia novaTransferencia = criarNovaTransferencia(txtJogador, txtOrigem, txtDestino, txtValor, dtData, cbStatus);
            if (novaTransferencia.validarTransferencia()) {
                List<MercadoDeTransferencia> transferencias = TransferenciaDAO.carregar();
                atualizarTransferencias(novaTransferencia, transferencias);
                TransferenciaDAO.salvar(transferencias);
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

    private MercadoDeTransferencia criarNovaTransferencia(TextField txtJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus) {
        int id = transferencia != null ? transferencia.getId() : 0;
        MercadoDeTransferencia novaTransferencia = new MercadoDeTransferencia(
                id,
                txtJogador.getText(),
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

    private void adicionarComponentesAoGrid(GridPane grid, TextField txtJogador, TextField txtOrigem, TextField txtDestino, TextField txtValor, DatePicker dtData, ComboBox<MercadoDeTransferencia.StatusTransferencia> cbStatus, Button btnSalvar) {
        grid.addRow(0, new Label("Jogador:"), txtJogador);
        grid.addRow(1, new Label("Clube Origem:"), txtOrigem);
        grid.addRow(2, new Label("Clube Destino:"), txtDestino);
        grid.addRow(3, new Label("Valor (milhões €):"), txtValor);
        grid.addRow(4, new Label("Data:"), dtData);
        grid.addRow(5, new Label("Status:"), cbStatus);
        grid.add(btnSalvar, 1, 6);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

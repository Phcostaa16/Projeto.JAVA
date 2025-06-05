package com.example.projeto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;

public class TransferenciaListScreen {
    private TableView<MercadoDeTransferencia> table;
    private ObservableList<MercadoDeTransferencia> transferencias;

    public void show() {
        Stage stage = new Stage();
        table = new TableView<>();

        // Criação das colunas
        criarColunas();

        // Carrega os dados
        carregarDados();

        // Criação dos botões
        HBox botoes = criarBotoes(stage);

        // Configuração do layout
        VBox layout = new VBox(10, table, botoes);
        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Lista de Transferências");
        stage.setScene(scene);
        stage.show();
    }

    private void criarColunas() {
        table.getColumns().addAll(
                criarColuna("Jogador", "jogador"),
                criarColuna("Clube Origem", "clubeOrigem"),
                criarColuna("Clube Destino", "clubeDestino"),
                criarColuna("Valor (€)", "valorFormatado"),
                criarColuna("Status", "status")
        );
    }

    private TableColumn<MercadoDeTransferencia, String> criarColuna(String nome, String propriedade) {
        TableColumn<MercadoDeTransferencia, String> coluna = new TableColumn<>(nome);
        coluna.setCellValueFactory(new PropertyValueFactory<>(propriedade));
        return coluna;
    }

    private HBox criarBotoes(Stage stage) {
        Button btnEditar = new Button("Editar");
        btnEditar.setOnAction(e -> editarTransferencia());

        Button btnExcluir = new Button("Excluir");
        btnExcluir.setOnAction(e -> excluirTransferencia());

        Button btnFechar = new Button("Fechar");
        btnFechar.setOnAction(e -> stage.close());

        Button btnAtualizar = new Button("Atualizar Lista");
        btnAtualizar.setOnAction(e -> carregarDados());

        return new HBox(10, btnEditar, btnExcluir, btnAtualizar, btnFechar);
    }

    private void carregarDados() {
        try {
            List<MercadoDeTransferencia> lista = TransferenciaDAO.carregar();
            transferencias = FXCollections.observableArrayList(lista);
            table.setItems(transferencias);
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void editarTransferencia() {
        MercadoDeTransferencia selecionada = table.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            new TransferenciaForm(selecionada).show();
        } else {
            mostrarAlerta("Selecione uma transferência para editar!");
        }
    }

    private void excluirTransferencia() {
        MercadoDeTransferencia selecionada = table.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                transferencias.remove(selecionada);
                TransferenciaDAO.salvar(transferencias);
            } catch (IOException e) {
                mostrarAlerta("Não foi possível excluir: " + e.getMessage());
            } catch (Exception e) {
                mostrarAlerta("Ocorreu um erro inesperado: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione uma transferência para excluir!");
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

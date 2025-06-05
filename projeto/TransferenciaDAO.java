package com.example.projeto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaDAO {
    private static final String FILE_NAME = "transferencias.dat";

    public static void salvar(List<MercadoDeTransferencia> transferencias) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(transferencias);
        }
    }

    public static List<MercadoDeTransferencia> carregar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<MercadoDeTransferencia>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }
}

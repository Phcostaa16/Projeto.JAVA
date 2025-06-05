package com.example.projeto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private static final String FILE_NAME = "players.dat";

    public static void save(List<Player> players) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(players);
        }
    }

    public static List<Player> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Player>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Return empty list on error
        }
    }
}

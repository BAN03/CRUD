package com.ban03;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GUIController {

    private String selected_table = null;
    Stage hola = new Stage();
    private Stage[] escenas;
    @FXML
    private ChoiceBox<String> tablas = new ChoiceBox<>();

    @FXML
    public void initialize() {
        try {

            ResultSet tables = new DBConecction().DBC().createStatement().executeQuery("SHOW TABLES");
            int tableCount = 0;
            while (tables.next()) {
                tablas.getItems().add(tables.getString(1));
                tableCount++;
            }
            this.escenas = new Stage[tableCount];

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // tablas.getItems().addAll("categorias", "clientes", "detalles_pedido",
        // "pedidos", "productos");
        tablas.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            this.selected_table = newValue;
        });

    }

    @FXML
    public void cargar() throws SQLException {
        if (this.selected_table != null) {
            try {
                TablesController tab = new TablesController(selected_table);
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Tables.fxml"));
                fxmlLoader.setController(tab);
                Scene scene = new Scene(fxmlLoader.load());
                for (int i = 0; i < escenas.length; i++) {
                    if (escenas[i] == null || !escenas[i].isShowing()) {
                        escenas[i] = new Stage();
                        escenas[i].setTitle(this.selected_table);
                        escenas[i].setScene(scene);
                        escenas[i].show();
                        break;
                    }
                    if (escenas[i].isShowing() && escenas[i].getTitle().equals(selected_table)) {
                        escenas[i].toFront();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}

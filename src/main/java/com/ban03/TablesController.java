package com.ban03;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.sql.*;

public class TablesController {
    @FXML
    TableView<DataRow> tableView = new TableView<>();
    private String table;
    private String SQL_SELECT;
    private String SQL_INSERT;
    private String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona=?";
    private String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";

    public static class DataRow {
        private final ObservableList<SimpleStringProperty> values;

        public DataRow(ObservableList<SimpleStringProperty> values) {
            this.values = values;
        }

        public ObservableList<SimpleStringProperty> getValues() {
            return values;
        }
    }

    public TablesController(String table) {
        this.table = table;
        this.SQL_SELECT = "SELECT * FROM " + table;
        // this.SQL_INSERT = "INSERT INTO " + table + " (nombre, apellido, email,
        // telefono) VALUES (?,?,?,?)";
    }

    @FXML
    public void initialize() {
        ObservableList<DataRow> data = getDataFromDatabase(tableView);
        tableView.setItems(data);
        tableView.setEditable(true);
    }

    private ObservableList<DataRow> getDataFromDatabase(TableView<DataRow> tableView) {
        ObservableList<DataRow> data = FXCollections.observableArrayList();
        try (
                Connection conn = new DBConecction().DBC();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + this.table)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i - 1;
                TableColumn<DataRow, String> column = new TableColumn<>(metaData.getColumnName(i));
                column.setCellValueFactory(
                        cellData -> cellData.getValue().getValues().get(columnIndex));
                column.setOnEditCommit(event -> {
                    DataRow dataRow = event.getRowValue();
                    dataRow.getValues();
                });
                tableView.getColumns().add(column);
            }
            while (resultSet.next()) {
                ObservableList<SimpleStringProperty> rowValues = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    rowValues.add(new SimpleStringProperty(resultSet.getString(i)));
                }
                data.add(new DataRow(rowValues));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @FXML
    protected void salir(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}

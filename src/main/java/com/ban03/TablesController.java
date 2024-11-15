package com.ban03;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.*;

public class TablesController {
    @FXML
    TableView<DataRow> tableView = new TableView<>();
    private ObservableList<DataRow> data;
    private String table;
    private String SQL_SELECT;

    public TablesController(String table) {
        this.table = table;
        this.SQL_SELECT = "SELECT * FROM " + this.table;
    }

    public static class DataRow {
        private ObservableList<SimpleStringProperty> values;

        public DataRow(ObservableList<SimpleStringProperty> values) {
            this.values = values;
        }

        public void setValues(ObservableList<SimpleStringProperty> values) {
            this.values = values;
        }

        public ObservableList<SimpleStringProperty> getValues() {
            return values;
        }
    }

    @FXML
    public void initialize() throws SQLException {
        this.data = FXCollections.observableArrayList();
        tableView.setEditable(true);
        cargar();
    }

    private void cargar() throws SQLException {
        data.clear();
        try (
                Connection conn = new DBConecction().DBC();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(this.SQL_SELECT)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i - 1;
                TableColumn<DataRow, String> column = new TableColumn<>(metaData.getColumnName(i));
                column.setCellValueFactory(
                        cellData -> cellData.getValue().getValues().get(columnIndex));
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                column.setOnEditCommit(event -> {
                    try {
                        String query = "UPDATE " + this.table + " SET "
                                + metaData.getColumnName(event.getTablePosition().getColumn() + 1) + " = "
                                + "'" + event.getNewValue() + "'" + " WHERE " + metaData.getColumnName(1) + " = "
                                + event.getRowValue().getValues().get(0).getValue();
                        new DBConecction().DBC().createStatement().execute(query);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
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
            System.out.println(e.getMessage());
        }
        tableView.setItems(data);
    }

    @FXML
    public void eliminar() {

        String query = "DELETE FROM " + this.table + " WHERE " + this.tableView.getColumns().get(0).getText() + " = "
                + this.tableView.getSelectionModel().getSelectedItem().getValues().get(0).getValue();
        try {
            new DBConecction().DBC().createStatement().execute(query);
            cargar();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void salir(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

}

package org.malisha.chatappjavafx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

class CustomListCell extends ListCell<String> {
//    public CustomListCell(ListView<String> listView) {
//        // Add a listener to detect selection changes
//        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//            // Update the cell style based on the selection
//            updateCellStyle();
//        });
//    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setPrefHeight(50); // Set the height here
            setStyle("-fx-control-inner-background: #d9d9d9;");
            setStyle("-fx-background-color: #d9d9d9; -fx-selection-bar-non-focused: #d9d9d9;");
            setStyle("fx-selection-bar: #dce8ff; -fx-selection-bar-focused: #d9d9d9");
        }
    }

//    private void updateCellStyle() {
//        ListView<String> listView = getListView();
//        if (listView != null && isSelected()) {
//            // Cell is selected
//            setStyle("-fx-background-color: #000000; -fx-selection-bar-non-focused: green;");
//        } else {
//            // Cell is not selected
//            //setStyle("-fx-background-color: white; fx-selection-bar:green");
//        }
//    }
}
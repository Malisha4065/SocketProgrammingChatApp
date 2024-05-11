package org.malisha.chatappjavafx;

import javafx.scene.control.Label;
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
//            setText(item);
////            setPrefHeight(50); // Set the height here
////            setStyle("-fx-control-inner-background: #d9d9d9;");
////            setStyle("-fx-background-color: #d9d9d9; -fx-selection-bar-non-focused: #d9d9d9;");
////            setStyle("fx-selection-bar: #dce8ff; -fx-selection-bar-focused: #d9d9d9");
//            int unseenMessages = 3; // You would set this based on your app logic
//
//            // Create a circle with number label
//            String circleStyle = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px;";
//            String numberLabel = String.valueOf(unseenMessages);
//            String circle = String.format("<Circle style='%s'>%s</Circle>", circleStyle, numberLabel);
//
//            // Set the graphic to the circle HTML
//            setGraphic(new javafx.scene.control.Label(circle));
            Label nameLabel = new Label();
            nameLabel.setText(item);

            // Example: set number of unseen messages
            int unseenMessages = 3; // Replace with your logic

            // Create a circle with number label
            String numberLabel = String.valueOf(unseenMessages);

            Label numberBadge = new Label(numberLabel);
            numberBadge.getStyleClass().add("number-badge");


            // Apply styles to the number badge
            numberBadge.setStyle("-fx-background-color: red; -fx-text-fill: white; " +
                    "-fx-font-size: 10px; -fx-padding: 2px; -fx-min-width: 16px; " +
                    "-fx-min-height: 16px; -fx-background-radius: 50%;");
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
package pl.wszib.edu.pocketdesktop.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;

@UtilityClass
class CommonUtils {

  static <T> void removeDuplicate(List<T> list) {

    Set<T> set = new HashSet<>(list);
    list.clear();
    list.addAll(set);
  }

  static void addEventToButton(
      DialogPane dialogPane, ButtonData buttonData, EventHandler<ActionEvent> eventHandler) {

    dialogPane.getButtonTypes().stream()
        .filter(buttonType -> buttonType.getButtonData() == buttonData)
        .findFirst()
        .ifPresent(button -> dialogPane.lookupButton(button)
            .addEventFilter(ActionEvent.ACTION, eventHandler));
  }

  static void closeDialog(ActionEvent event) {

    Node source = (Node) event.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  static void showInfoDialogWithContent(String title, String headerText, String contentText,
      AlertType alertType) {

    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    alert.showAndWait();
  }

  static void showInfoDialog(String title, String headerText, AlertType alertType) {

    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.showAndWait();
  }
}

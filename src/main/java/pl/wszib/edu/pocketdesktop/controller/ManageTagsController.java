package pl.wszib.edu.pocketdesktop.controller;

import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.addEventToButton;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.closeDialog;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.showInfoDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import pl.wszib.edu.pocketdesktop.client.modify.ModifyClient;

@Controller
public class ManageTagsController {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private DialogPane manageTagsDialog;
  @FXML
  private TableColumn<String, Void> colDelete;
  @FXML
  private TableColumn<String, String> colTag;
  @FXML
  private TableView<String> tagsTable;

  private ModifyClient modifyClient;

  private ObservableList<String> allTagsList = FXCollections.observableArrayList();
  private List<String> tagsToDelete = new ArrayList<>();
  private Map<String, String> tagsToRename = new HashMap<>();

  @FXML
  void initialize() {

    addEventToButton(manageTagsDialog, ButtonData.APPLY, this::handleApplyButtonClick);
    addEventToButton(manageTagsDialog, ButtonData.CANCEL_CLOSE, this::handleCancelButtonClick);
  }

  public void setRequiredFields(ModifyClient modifyClient, List<String> tagList) {

    this.modifyClient = modifyClient;

    allTagsList.clear();
    allTagsList.addAll(tagList);
    setTagsColumn();
  }

  private void setTagsColumn() {

    colTag.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    colTag.setCellFactory(TextFieldTableCell.forTableColumn());
    colTag.setOnEditCommit(event -> {
      String oldTag = event.getOldValue();
      String newTag = event.getNewValue();
      tagsToRename.put(oldTag, newTag);
    });
    tagsTable.setItems(allTagsList);

    colDelete.setCellFactory(column -> new TableCell<String, Void>() {
      private final MaterialDesignIconView deleteIcon = new MaterialDesignIconView(
          MaterialDesignIcon.CLOSE);

      {
        deleteIcon.setOnMouseClicked(event -> {
          String selectedTag = getTableView().getItems().get(getIndex());
          tagsToDelete.add(selectedTag);
          allTagsList.remove(selectedTag);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(deleteIcon);
          setAlignment(Pos.CENTER);
        }
      }
    });
  }

  private void handleCancelButtonClick(ActionEvent event) {

    closeDialog(event);
  }

  private void handleApplyButtonClick(ActionEvent event) {

    try {
      deleteTagsIfPresent();
      renameTagsIfPresent();
    } catch (HttpClientErrorException exception) {
      exception.printStackTrace();
      showErrorDialog();
    } finally {
      closeDialog(event);
    }
  }

  private void deleteTagsIfPresent() {

    if (!tagsToDelete.isEmpty()) {
      modifyClient.deleteTags(tagsToDelete);
    }
  }

  private void renameTagsIfPresent() {

    if (!tagsToRename.isEmpty()) {
      modifyClient.renameTags(tagsToRename);
    }
  }

  private void showErrorDialog() {

    showInfoDialog("Error", "Something went wrong while editing tags!", AlertType.ERROR);
  }
}

package pl.wszib.edu.pocketdesktop.controller;

import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.addEventToButton;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.closeDialog;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.removeDuplicate;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.showInfoDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.CheckComboBox;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import pl.wszib.edu.pocketdesktop.client.modify.ModifyClient;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;

@Controller
public class ManageItemTagsController {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private CheckComboBox<String> chosenTagsCombo;
  @FXML
  private Button clearTagsButton;
  @FXML
  private Button addChosenTagsButton;
  @FXML
  private TableColumn<String, Void> colDelete;
  @FXML
  private TableColumn<String, String> colTag;
  @FXML
  private DialogPane manageItemTagsDialog;
  @FXML
  private TextField newTagsTextField;
  @FXML
  private TableView<String> tagsTable;

  private ModifyClient modifyClient;

  private Item selectedItem;
  private List<String> tagsToUpdate = new ArrayList<>();

  public ObservableList<String> allTagsList = FXCollections.observableArrayList();
  public ObservableList<String> allItemTagsList = FXCollections.observableArrayList();

  @FXML
  void addChosenTags(ActionEvent event) {

    ObservableList<String> listOfChosenTags = chosenTagsCombo.getCheckModel().getCheckedItems();
    String insertedText = newTagsTextField.getText();

    if (!listOfChosenTags.isEmpty()) {
      tagsToUpdate.addAll(listOfChosenTags);
      allItemTagsList.addAll(listOfChosenTags);
    }

    if (!insertedText.isEmpty()) {
      List<String> listOfNewTags = Arrays.asList(insertedText.split(","));
      tagsToUpdate.addAll(listOfNewTags);
      allItemTagsList.addAll(listOfNewTags);
    }

    removeDuplicate(tagsToUpdate);
    removeDuplicate(allItemTagsList);
  }

  @FXML
  void clearAllTags(ActionEvent event) {

    allItemTagsList.clear();
    tagsToUpdate.clear();
  }

  @FXML
  void initialize() {

    addEventToButton(manageItemTagsDialog, ButtonData.APPLY, this::handleApplyButtonClick);
    addEventToButton(manageItemTagsDialog, ButtonData.CANCEL_CLOSE, this::handleCancelButtonClick);
  }

  private void handleCancelButtonClick(ActionEvent event) {

    closeDialog(event);
  }

  private void handleApplyButtonClick(ActionEvent event) {

    try {
      modifyClient.replaceTags(selectedItem.getItemId(), tagsToUpdate);
    } catch (HttpClientErrorException exception) {
      exception.printStackTrace();
      showErrorDialog();
    } finally {
      closeDialog(event);
    }
  }

  public void setRequiredFields(Item selectedItem, ModifyClient modifyClient,
      ObservableList<String> allTags) {

    this.selectedItem = selectedItem;
    this.allTagsList = allTags;
    this.modifyClient = modifyClient;

    loadItemTagList();
    loadComboTagList();
    setTagsColumn();

    if (selectedItem.getTags() != null) {
      tagsToUpdate.addAll(selectedItem.getTags());
    }
  }

  private void loadItemTagList() {

    allItemTagsList.clear();

    if (selectedItem.getTags() != null) {
      allItemTagsList.addAll(selectedItem.getTags());
    }
  }

  private void loadComboTagList() {

    chosenTagsCombo.getItems().clear();
    chosenTagsCombo.getItems().addAll(allTagsList);
  }

  private void setTagsColumn() {

    colTag.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    colTag.setCellFactory(TextFieldTableCell.forTableColumn());
    colTag.setOnEditCommit(event -> {
      String oldTag = event.getOldValue();
      tagsToUpdate.remove(oldTag);
      String newTag = event.getNewValue();
      tagsToUpdate.add(newTag);
    });
    tagsTable.setItems(allItemTagsList);

    colDelete.setCellFactory(column -> new TableCell<String, Void>() {
      private final MaterialDesignIconView deleteIcon = new MaterialDesignIconView(
          MaterialDesignIcon.CLOSE);

      {
        deleteIcon.setOnMouseClicked(event -> {
          String selectedTag = getTableView().getItems().get(getIndex());
          allItemTagsList.remove(selectedTag);
          tagsToUpdate.remove(selectedTag);
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

  private void showErrorDialog() {

    showInfoDialog("Error", "Something went wrong while editing tags!", AlertType.ERROR);
  }
}

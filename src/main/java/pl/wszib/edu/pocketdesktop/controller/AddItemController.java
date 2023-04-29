package pl.wszib.edu.pocketdesktop.controller;

import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.addEventToButton;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.closeDialog;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.showInfoDialogWithContent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import pl.wszib.edu.pocketdesktop.client.add.AddClient;
import pl.wszib.edu.pocketdesktop.client.add.model.AddItemData;

@Controller
public class AddItemController {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private DialogPane addItemDialog;
  @FXML
  private CheckComboBox<String> itemTags;
  @FXML
  private TextField itemTitle;
  @FXML
  private TextField itemUrl;

  private AddClient addClient;

  private ObservableList<String> allTagsList = FXCollections.observableArrayList();

  @FXML
  void initialize() {

    addEventToButton(addItemDialog, ButtonData.APPLY, this::handleApplyButtonClick);
    addEventToButton(addItemDialog, ButtonData.CANCEL_CLOSE, this::handleCancelButtonClick);
  }

  public void setRequiredFields(AddClient addClient, List<String> allTags) {

    this.addClient = addClient;

    allTagsList.clear();
    allTagsList.addAll(allTags);
    itemTags.getItems().addAll(allTagsList);
  }

  private void handleApplyButtonClick(ActionEvent event) {

    AddItemData addItemData = new AddItemData();
    ObservableList<String> listOfChosenTags = itemTags.getCheckModel().getCheckedItems();
    addItemData.setTags(listOfChosenTags);

    addItemData.setUrl(itemUrl.getText());
    addItemData.setTitle(itemTitle.getText());

    if (validateEnteredData(addItemData)) {
      try {
        addClient.addItem(addItemData);
        showInfoDialogWithContent("Information", null,
            "Your item has been successfully added to your GetPocket profile!",
            AlertType.INFORMATION);
      } catch (HttpClientErrorException exception) {
        exception.printStackTrace();
        showErrorDialog();
        closeDialog(event);
      }
      closeDialog(event);
    }
  }

  private void handleCancelButtonClick(ActionEvent event) {

    closeDialog(event);
  }

  private boolean validateEnteredData(AddItemData addItemData) {

    boolean isDataValid = true;
    String urlDefaultStyle = itemUrl.getStyle();
    String titleDefaultStyle = itemTitle.getStyle();
    String emptyFieldStyle = "-fx-border-color: #b30707;";

    String url = addItemData.getUrl();
    String title = addItemData.getTitle();

    if (url.isEmpty() || !isValidUrl(url)) {
      itemUrl.setStyle(emptyFieldStyle);
      isDataValid = false;
    } else {
      itemUrl.setStyle(urlDefaultStyle);
    }

    if (title.isEmpty()) {
      itemTitle.setStyle(emptyFieldStyle);
      isDataValid = false;
    } else {
      itemTitle.setStyle(titleDefaultStyle);
    }

    return isDataValid;
  }

  private boolean isValidUrl(String url) {

    String validUrlPattern = "^(https?:\\/\\/)?(www\\.)?([a-zA-Z0-9]+(-?[a-zA-Z0-9]+)*\\.)+[\\w]{2,}(\\/\\S*)?$";
    Pattern urlPattern = Pattern.compile(validUrlPattern);
    Matcher matcher = urlPattern.matcher(url);
    return matcher.matches();
  }

  private void showErrorDialog() {

    showInfoDialogWithContent("Error", "Something went wrong while adding new item!",
        "Please enter a valid URL", AlertType.ERROR);
  }
}

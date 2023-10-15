package pl.wszib.edu.pocketdesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wszib.edu.pocketdesktop.client.add.AddClient;
import pl.wszib.edu.pocketdesktop.client.download.ItemDownloader;
import pl.wszib.edu.pocketdesktop.client.modify.ModifyClient;
import pl.wszib.edu.pocketdesktop.client.modify.model.ModifyOption;
import pl.wszib.edu.pocketdesktop.client.retrieve.RetrieveClient;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.ItemsFilterOptions;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;
import pl.wszib.edu.pocketdesktop.config.FxmlView;
import pl.wszib.edu.pocketdesktop.config.StageManager;

@Controller
@RequiredArgsConstructor
public class MainController {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button addItemButton;
  @FXML
  private MenuItem editTags;
  @FXML
  private MenuItem archiveItem;
  @FXML
  private MenuItem downloadItem;
  @FXML
  private MenuItem deleteItem;
  @FXML
  private Button filterButton;
  @FXML
  private Button refreshButton;
  @FXML
  private Button manageTagsButton;
  @FXML
  private CheckComboBox<String> tagsComboBox;
  @FXML
  private CheckBox favoriteItemsCheckBox;
  @FXML
  private TableView<Item> itemsTable;
  @FXML
  private TableColumn<Item, Boolean> columnFavorite;
  @FXML
  private TableColumn<Item, String> columnGivenTitle;
  @FXML
  private TableColumn<Item, String> columnGivenUrl;
  @FXML
  private TableColumn<Item, String> columnItemId;
  @FXML
  private TableColumn<Item, String> columnResolvedTitle;
  @FXML
  private TableColumn<Item, String> columnTags;

  @Lazy
  @Autowired
  private StageManager stageManager;
  private final RetrieveClient retrieveClient;
  private final AddClient addClient;
  private final ModifyClient modifyClient;
  private final ItemDownloader itemDownloader;
  private final HostServices hostServices;
  private ObservableList<Item> allItemsList = FXCollections.observableArrayList();
  public ObservableList<String> allTagsList = FXCollections.observableArrayList();
  private boolean favoriteField;

  @FXML
  void initialize() {

    loadItemList(retrieveClient.retrieveItemList());
    loadTagList();
    setItemColumns();
    setTagsCombobox();
  }

  private void loadItemList(List<Item> itemsToView) {

    allItemsList.clear();
    allItemsList.addAll(itemsToView);

    itemsTable.setItems(allItemsList);
  }

  private void loadTagList() {

    allTagsList.clear();
    allTagsList.addAll(retrieveClient.getTagList(allItemsList));
  }

  private void setItemColumns() {

    columnItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
    columnGivenTitle.setCellValueFactory(new PropertyValueFactory<>("givenTitle"));
    columnResolvedTitle.setCellValueFactory(new PropertyValueFactory<>("resolvedTitle"));
    columnTags.setCellValueFactory(new PropertyValueFactory<>("tags"));
    configureUrlColumn();
    configureFavoriteColumn();
  }

  private void setTagsCombobox() {

    tagsComboBox.getItems().clear();
    tagsComboBox.getItems().addAll(allTagsList);
  }

  private void configureUrlColumn() {

    columnGivenUrl.setCellValueFactory(new PropertyValueFactory<>("givenUrl"));
    columnGivenUrl.setCellFactory(column -> new TableCell<Item, String>() {

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(createHyperlink(item, hostServices));
      }
    });
  }

  @FXML
  void refresh(ActionEvent event) {

    List<Item> allItems = retrieveClient.retrieveItemList();
    loadItemList(allItems);

    loadTagList();
    setTagsCombobox();
    tagsComboBox.getCheckModel().clearChecks();
  }

  private Hyperlink createHyperlink(String item, HostServices hostServices) {

    if (item == null || item.isEmpty()) {
      return null;
    }

    Hyperlink hyperlink = new Hyperlink(item);
    hyperlink.setOnAction(event -> {
      hostServices.showDocument(item);
    });

    return hyperlink;
  }

  private void configureFavoriteColumn() {

    columnFavorite.setEditable(true);
    columnFavorite.setCellValueFactory(cellData -> {
      Item item = cellData.getValue();
      SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(item.isFavorite());

      // Add listener to handle change if checkbox is selected or not
      booleanProp.addListener((observable, oldValue, newValue) -> {
        item.setIsFavorite(newValue);
        setFavoriteValue(item, newValue);
      });

      return booleanProp;
    });
    columnFavorite.setCellFactory(item -> new CheckBoxTableCell<>());
  }

  void setFavoriteValue(Item item, boolean newValue) {

    String itemId = item.getItemId();

    if (newValue) {
      modifyClient.modifyItem(itemId, ModifyOption.FAVORITE);
    } else {
      modifyClient.modifyItem(itemId, ModifyOption.UNFAVORITE);
    }
  }

  @FXML
  void addItem(ActionEvent event) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource(FxmlView.ADD_ITEM.getFxmlFile()));
      DialogPane addItemDialogPane = fxmlLoader.load();
      Scene scene = new Scene(addItemDialogPane);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle(FxmlView.ADD_ITEM.getTitle());

      AddItemController addItemController = fxmlLoader.getController();
      addItemController.setRequiredFields(addClient, allTagsList);

      stage.showAndWait();
      refresh(event);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  @FXML
  void manageTags(ActionEvent event) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource(FxmlView.MANAGE_TAGS.getFxmlFile()));

      DialogPane manageTagsPane = fxmlLoader.load();
      Scene scene = new Scene(manageTagsPane);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle(FxmlView.MANAGE_TAGS.getTitle());

      ManageTagsController manageTagsController = fxmlLoader.getController();
      manageTagsController.setRequiredFields(modifyClient, allTagsList);

      stage.showAndWait();
      refresh(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void editTags(ActionEvent event) {

    Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource(FxmlView.MANAGE_ITEM_TAGS.getFxmlFile()));
      DialogPane manageItemTagsDialogPane = fxmlLoader.load();

      Scene scene = new Scene(manageItemTagsDialogPane);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("Manage tags for: " + selectedItem.getGivenTitle());

      ManageItemTagsController manageItemTagsController = fxmlLoader.getController();
      manageItemTagsController.setRequiredFields(selectedItem, modifyClient, allTagsList);

      stage.showAndWait();
      refresh(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void archiveItem(ActionEvent event) {

    String itemId = getSelectedItemId();
    if (isModifyActionConfirmedByUser("Archive item?",
        "Are you sure you want to archive this item?")) {
      modifyClient.modifyItem(itemId, ModifyOption.ARCHIVE);
      refresh(event);
    }
  }

  @FXML
  void deleteItem(ActionEvent event) {

    String itemId = getSelectedItemId();
    if (isModifyActionConfirmedByUser("Delete item?",
        "Are you sure you want to delete this item? You can't undo this operation")) {
      modifyClient.modifyItem(itemId, ModifyOption.DELETE);
      refresh(event);
    }
  }

  @FXML
  void downloadItem(ActionEvent event) throws IOException {

    Item selectedRecord = itemsTable.getSelectionModel().getSelectedItem();

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource(FxmlView.DOWNLOAD_ITEM.getFxmlFile()));

      DialogPane downloadItemDialogPane = fxmlLoader.load();
      Scene scene = new Scene(downloadItemDialogPane);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle(FxmlView.DOWNLOAD_ITEM.getTitle());

      DownloadItemController downloadItemController = fxmlLoader.getController();
      downloadItemController.setRequiredFields(selectedRecord.getGivenUrl(), itemDownloader);

      stage.showAndWait();
      refresh(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getSelectedItemId() {

    Item selectedRecord = itemsTable.getSelectionModel().getSelectedItem();
    if (selectedRecord != null) {
      return selectedRecord.getItemId();
    }
    return null;
  }

  @FXML
  void setFavoriteFilter(ActionEvent event) {

    favoriteField = favoriteItemsCheckBox.isSelected();
  }

  @FXML
  void filter(ActionEvent event) {

    ObservableList<String> listOfChosenTags = tagsComboBox.getCheckModel().getCheckedItems();

    ItemsFilterOptions itemsFilterOptions = new ItemsFilterOptions(favoriteField, listOfChosenTags);

    List<Item> filteredItems = retrieveClient.retrieveFilteredItemList(
        itemsFilterOptions);
    loadItemList(filteredItems);
  }

  private boolean isModifyActionConfirmedByUser(String confirmationQuestionTitle,
      String outcomeInformation) {

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(confirmationQuestionTitle);
    alert.setHeaderText(outcomeInformation);
    alert.setContentText(null);

    Optional<ButtonType> result = alert.showAndWait();

    return result.get() == ButtonType.OK;
  }
}

package pl.wszib.edu.pocketdesktop.controller;

import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.closeDialog;
import static pl.wszib.edu.pocketdesktop.controller.CommonUtils.showInfoDialog;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.springframework.stereotype.Controller;
import pl.wszib.edu.pocketdesktop.client.download.ItemDownloader;

@Controller
public class DownloadItemController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private DialogPane downloadItemDialog;

  @FXML
  private Button browseButton;

  @FXML
  private Button saveButton;

  @FXML
  private TextField directoryPath;

  @FXML
  private TextField fileName;

  private ItemDownloader itemDownloader;
  private String urlToDownload;
  private String fileNameToSave;
  private String filePathToSave;
  private File file = new File("");

  @FXML
  void initialize() {

  }

  public void setRequiredFields(String urlToDownload, ItemDownloader itemDownloader) {

    this.urlToDownload = urlToDownload;
    this.itemDownloader = itemDownloader;
  }

  @FXML
  void browse(ActionEvent event) {

    final DirectoryChooser directoryChooser = new DirectoryChooser();
    file = directoryChooser.showDialog(null);

    if (file != null) {
      directoryPath.setText(file.getAbsolutePath());
    }
  }

  @FXML
  void handleSaveButtonClick(ActionEvent event) {

    fileNameToSave = fileName.getText();
    filePathToSave = directoryPath.getText();

    if (validateEnteredData()) {
      try {
        itemDownloader.downloadItem(file, fileNameToSave, urlToDownload);
        showInfoDialog("Success", "Your item has been successfully downloaded!",
            AlertType.INFORMATION);
      } catch (Exception exception) {
        exception.printStackTrace();
        showErrorDialog();
        closeDialog(event);
      }
      closeDialog(event);
    }
  }

  private boolean validateEnteredData() {

    boolean isDataValid = true;

    String fileNameDefaultStyle = fileName.getStyle();
    String pathDefaultStyle = directoryPath.getStyle();
    String emptyFieldStyle = "-fx-border-color: #b30707;";

    if (fileNameToSave.isEmpty() || !isValidFileName(fileNameToSave)) {
      fileName.setStyle(emptyFieldStyle);
      isDataValid = false;
    } else {
      fileName.setStyle(fileNameDefaultStyle);
    }

    if (filePathToSave.isEmpty()) {
      directoryPath.setStyle(emptyFieldStyle);
      isDataValid = false;
    } else {
      directoryPath.setStyle(pathDefaultStyle);
    }

    return isDataValid;
  }

  private boolean isValidFileName(String name) {

    String validPathPatter = "([A-Z]|[a-z]|[0-9]|_|-|\\.|\\s)+";
    Pattern pathPattern = Pattern.compile(validPathPatter);
    Matcher matcher = pathPattern.matcher(name);
    return matcher.matches();
  }

  private void showErrorDialog() {

    showInfoDialog("Error", "Something went wrong while downloading your item. ", AlertType.ERROR);
  }
}

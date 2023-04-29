package pl.wszib.edu.pocketdesktop.config;

import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FxmlView {

  AUTHORIZATION("authorization.title", "/fxml/Authorization.fxml"),
  MAIN("main.title", "/fxml/Main.fxml"),
  ADD_ITEM("add.item", "/fxml/AddItem.fxml"),
  DOWNLOAD_ITEM("download.item", "/fxml/DownloadItem.fxml"),
  MANAGE_TAGS("manage.tags", "/fxml/ManageTags.fxml"),
  MANAGE_ITEM_TAGS("manage.item.tags", "/fxml/ManageItemTags.fxml");

  private final String titleKey;
  private final String fxmlFile;

  public String getTitle() {
    return getStringFromBundle(titleKey);
  }

  public String getFxmlFile() {
    return fxmlFile;
  }

  private String getStringFromBundle(String key) {

    return ResourceBundle.getBundle("Bundle").getString(key);
  }
}
package pl.wszib.edu.pocketdesktop.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wszib.edu.pocketdesktop.config.FxmlView;
import pl.wszib.edu.pocketdesktop.client.authorization.PocketAuthorizationClient;
import pl.wszib.edu.pocketdesktop.config.StageManager;

@Controller
public class AuthorizationController implements Initializable {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private WebView webView;

  @Autowired
  private PocketAuthorizationClient client;
  @Lazy
  @Autowired
  private StageManager stageManager;

  @FXML
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    String url = client.getAuthorizationUrl();

    WebEngine webEngine = webView.getEngine();
    webEngine.load(url);

    webEngine.locationProperty().addListener((observableValue, oldLocation, newLocation) -> {

      if (newLocation.startsWith("http://google.pl")) {
        client.setAccessToken();
        stageManager.switchScene(FxmlView.MAIN);
      }
    });
  }
}

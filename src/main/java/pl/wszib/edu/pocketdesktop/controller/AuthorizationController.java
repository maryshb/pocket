package pl.wszib.edu.pocketdesktop.controller;

import com.teamdev.jxbrowser.navigation.Navigation;
import com.teamdev.jxbrowser.navigation.internal.rpc.LoadFinished;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wszib.edu.pocketdesktop.client.authorization.PocketAuthorizationClient;
import pl.wszib.edu.pocketdesktop.config.FxmlBrowserView;
import pl.wszib.edu.pocketdesktop.config.FxmlView;
import pl.wszib.edu.pocketdesktop.config.StageManager;

@Controller
public class AuthorizationController implements Initializable {

  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private TextField textField;
  @FXML
  private FxmlBrowserView browserView;
  @Autowired
  private PocketAuthorizationClient client;
  @Lazy
  @Autowired
  private StageManager stageManager;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    String url = client.getAuthorizationUrl();
    textField.setText(url);

    Navigation navigation = browserView.browser().navigation();
    navigation.loadUrl(url);

    navigation.on(LoadFinished.class, event -> {

      String currentUrl = browserView.browser().url();
      if (currentUrl.startsWith("https://www.google.pl")) {
        client.setAccessToken();
        stageManager.switchScene(FxmlView.MAIN);
      }
    });
  }

  public void loadUrl(ActionEvent actionEvent) {

    browserView.browser().navigation().loadUrl(textField.getText());
  }
}

package pl.wszib.edu.pocketdesktop.config;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringFXMLLoader {

  private final ResourceBundle resourceBundle;
  private final ApplicationContext context;

  public Parent load(String fxmlPath) throws IOException {

    FXMLLoader loader = new FXMLLoader();

    loader.setControllerFactory(context::getBean);
    loader.setResources(resourceBundle);
    loader.setLocation(getClass().getResource(fxmlPath));

    return loader.load();
  }
}

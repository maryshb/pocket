package pl.wszib.edu.pocketdesktop.config;

import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class StageManager {

  private final Stage primaryStage;
  private final SpringFXMLLoader springFXMLLoader;

  public void switchScene(final FxmlView view) {

    Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
    show(viewRootNodeHierarchy, view.getTitle());
  }

  private void show(final Parent rootnode, String title) {

    Scene scene = prepareScene(rootnode);

    primaryStage.setTitle(title);
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.centerOnScreen();
    primaryStage.setResizable(false);

    try {
      primaryStage.show();
    } catch (Exception exception) {
      logAndExit("Unable to show scene for title" + title, exception);
    }
  }

  private Scene prepareScene(Parent rootnode) {

    Scene scene = primaryStage.getScene();

    if (scene == null) {
      scene = new Scene(rootnode);
    }
    scene.setRoot(rootnode);
    return scene;
  }

  private Parent loadViewNodeHierarchy(String fxmlFilePath) {

    Parent rootNode = null;
    try {
      rootNode = springFXMLLoader.load(fxmlFilePath);
      Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
    } catch (Exception exception) {
      logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
    }
    return rootNode;
  }

  private void logAndExit(String errorMsg, Exception exception) {

    log.error(errorMsg, exception, exception.getCause());
    Platform.exit();
  }
}

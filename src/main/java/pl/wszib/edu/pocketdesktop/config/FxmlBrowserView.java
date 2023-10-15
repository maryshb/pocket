package pl.wszib.edu.pocketdesktop.config;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.scene.layout.StackPane;

public class FxmlBrowserView extends StackPane {

  private final Browser browser;

  /**
   * Constructs an instance of {@code FxmlBrowserView}.
   */
  public FxmlBrowserView() {

    Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
        .licenseKey("")
        .build());
    browser = engine.newBrowser();
    BrowserView view = BrowserView.newInstance(browser);
    getChildren().add(view);
  }

  /**
   * Returns the {@link Browser} instance of the current browser view.
   */
  public Browser browser() {

    return browser;
  }
}

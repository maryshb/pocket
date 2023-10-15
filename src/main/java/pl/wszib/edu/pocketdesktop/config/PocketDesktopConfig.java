package pl.wszib.edu.pocketdesktop.config;

import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
import pl.wszib.edu.pocketdesktop.PocketDesktopApplication;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;

@Configuration
class PocketDesktopConfig {

  @Autowired
  private SpringFXMLLoader springFXMLLoader;

  @Bean
  public ResourceBundle resourceBundle() {

    return ResourceBundle.getBundle("Bundle");
  }

  @Bean
  @Lazy
  public StageManager stageManager(Stage stage) {

    return new StageManager(stage, springFXMLLoader);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder
        .defaultHeader("X-Accept", "application/json")
        .defaultHeader("Content-Type", "application/json")
        .build();
  }

  @Bean
  public AuthorizedRequestBase authorizedRequestBase(@Value("${consumerKey}") String consumerKey,
      @Value("${accessToken}") String accessToken) {

    return new AuthorizedRequestBase(consumerKey, accessToken, "complete");
  }

  @Bean
  public HostServices hostServices(PocketDesktopApplication application) {

    return application.getHostServices();
  }
}

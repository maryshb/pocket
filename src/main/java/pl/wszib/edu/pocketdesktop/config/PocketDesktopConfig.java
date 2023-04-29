package pl.wszib.edu.pocketdesktop.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    HttpComponentsClientHttpRequestFactory useApacheHttpClient = new HttpComponentsClientHttpRequestFactory();
    useApacheHttpClient.setHttpClient(httpClient);

    return builder
        .additionalInterceptors(new LoggingInterceptor())
        .requestFactory(() -> useApacheHttpClient)
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

  public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {

      System.out.println("Request body: " + new String(body, StandardCharsets.UTF_8));
      return execution.execute(request, body);
    }
  }
}

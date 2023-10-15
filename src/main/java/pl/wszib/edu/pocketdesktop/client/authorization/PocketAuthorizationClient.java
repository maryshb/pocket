package pl.wszib.edu.pocketdesktop.client.authorization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;
import pl.wszib.edu.pocketdesktop.client.authorization.model.request.PocketAccessTokenRequest;
import pl.wszib.edu.pocketdesktop.client.authorization.model.request.PocketRequestTokenRequest;
import pl.wszib.edu.pocketdesktop.client.authorization.model.response.PocketAccessTokenResponse;
import pl.wszib.edu.pocketdesktop.client.authorization.model.response.PocketRequestTokenResponse;

@Service
public class PocketAuthorizationClient {

  private static final String POCKET_URL = "https://getpocket.com";
  private String requestToken;
  private String accessToken;

  @Autowired
  private RestTemplate restTemplate;
  @Value("${consumerKey}")
  private String consumerKey;
  @Autowired
  private ConfigurableEnvironment environment;
  @Autowired
  private AuthorizedRequestBase authorizedRequestBase;


  public PocketRequestTokenResponse getRequestToken(
      @RequestBody PocketRequestTokenRequest pocketRequestTokenRequest) {

    HttpEntity<PocketRequestTokenRequest> entity = new HttpEntity<>(pocketRequestTokenRequest);

    return restTemplate.postForObject(POCKET_URL + "/v3/oauth/request.php", entity,
        PocketRequestTokenResponse.class);
  }

  public String getAuthorizationUrl() {

    PocketRequestTokenRequest requestTokenRequest = new PocketRequestTokenRequest(consumerKey,
        "https://google.pl");
    PocketRequestTokenResponse requestTokenResponse = getRequestToken(requestTokenRequest);
    requestToken = requestTokenResponse.getCode();

    return POCKET_URL + "/auth/authorize?request_token=" + requestToken
        + "&redirect_uri=https://google.pl";
  }

  public void setAccessToken() {

    PocketAccessTokenRequest accessTokenRequest = new PocketAccessTokenRequest(consumerKey,
        requestToken);
    HttpEntity<PocketAccessTokenRequest> entity = new HttpEntity<>(accessTokenRequest);

    accessToken = Objects.requireNonNull(
        restTemplate.postForObject(POCKET_URL + "/v3/oauth/authorize.php", entity,
            PocketAccessTokenResponse.class)).getAccess_token();

    setAccessTokenProperty();
  }

  private void setAccessTokenProperty() {

    MutablePropertySources propertySources = environment.getPropertySources();
    Map<String, Object> source = new HashMap<>();
    source.put("accessToken", accessToken);
    propertySources.addFirst(new MapPropertySource("accessToken", source));
    authorizedRequestBase.setAccess_token(accessToken);
  }
}

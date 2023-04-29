package pl.wszib.edu.pocketdesktop.client.authorization.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PocketAuthorizationRequest {

  private String consumer_key;
  private String code;
  private String redirect_uri;
}

package pl.wszib.edu.pocketdesktop.client.authorization.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PocketRequestTokenRequest {

  private String consumer_key;
  private String redirect_uri;
}

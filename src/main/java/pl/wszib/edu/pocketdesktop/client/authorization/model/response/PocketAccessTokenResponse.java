package pl.wszib.edu.pocketdesktop.client.authorization.model.response;

import lombok.Data;

@Data
public class PocketAccessTokenResponse {

  private String access_token;
  private String username;
}

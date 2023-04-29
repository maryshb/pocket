package pl.wszib.edu.pocketdesktop.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthorizedRequestBase {

  private String consumer_key;
  private String access_token;
  private String detailType;

  public AuthorizedRequestBase(AuthorizedRequestBase authorizedRequestBase) {

    this.consumer_key = authorizedRequestBase.getConsumer_key();
    this.access_token = authorizedRequestBase.getAccess_token();
    this.detailType = authorizedRequestBase.getDetailType();
  }
}

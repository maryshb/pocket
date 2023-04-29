package pl.wszib.edu.pocketdesktop.client.retrieve.model.request;

import lombok.Data;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;

@Data
public class TaggedItemsRequest extends AuthorizedRequestBase {

  private String tag;

  public TaggedItemsRequest(String tag, AuthorizedRequestBase authorizedRequestBase) {

    super(authorizedRequestBase);
    this.tag = tag;
  }
}

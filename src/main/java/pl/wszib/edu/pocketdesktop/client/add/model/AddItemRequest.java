package pl.wszib.edu.pocketdesktop.client.add.model;

import java.util.List;
import lombok.Data;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;

@Data
public class AddItemRequest extends AuthorizedRequestBase {

  private String url;
  private String title;
  private List<String> tags;

  public AddItemRequest(AuthorizedRequestBase authorizedRequestBase, String url, String title,
      List<String> tags) {

    super(authorizedRequestBase);
    this.url = url;
    this.title = title;
    this.tags = tags;
  }
}

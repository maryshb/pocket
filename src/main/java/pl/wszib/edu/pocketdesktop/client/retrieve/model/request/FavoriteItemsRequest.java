package pl.wszib.edu.pocketdesktop.client.retrieve.model.request;

import lombok.Data;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;

@Data
public class FavoriteItemsRequest extends AuthorizedRequestBase {

  private final int favorite;

  public FavoriteItemsRequest(int isFavorite, AuthorizedRequestBase authorizedRequestBase) {

    super(authorizedRequestBase);
    this.favorite = isFavorite;
  }
}

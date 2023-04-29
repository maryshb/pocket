package pl.wszib.edu.pocketdesktop.client.modify.model;

import java.util.List;
import lombok.Data;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;

@Data
public class ModifyRequest<E> extends AuthorizedRequestBase {

  private List<E> actions;

  public ModifyRequest(List<E> actions, AuthorizedRequestBase authorizedRequestBase) {

    super(authorizedRequestBase);
    this.actions = actions;
  }
}

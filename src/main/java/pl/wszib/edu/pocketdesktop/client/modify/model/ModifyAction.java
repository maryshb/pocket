package pl.wszib.edu.pocketdesktop.client.modify.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyAction {

  private String action;
  private String item_id;

  public static List<ModifyAction> getActionList(String action, String itemId) {

    List<ModifyAction> actions = new ArrayList<>();
    actions.add(new ModifyAction(action, itemId));

    return actions;
  }
}

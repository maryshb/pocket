package pl.wszib.edu.pocketdesktop.client.modify.model.tags;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplaceItemTagsAction {

  private final String action = "tags_replace";
  private String item_id;
  private List<String> tags;
}

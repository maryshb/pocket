package pl.wszib.edu.pocketdesktop.client.modify.model.tags;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteTagAction {

  private final String action = "tag_delete";
  private String tag;
}

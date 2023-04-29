package pl.wszib.edu.pocketdesktop.client.modify.model.tags;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RenameTagAction {

  private final String action = "tag_rename";
  private String old_tag;
  private String new_tag;
}

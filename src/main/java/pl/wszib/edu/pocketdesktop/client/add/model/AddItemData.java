package pl.wszib.edu.pocketdesktop.client.add.model;

import java.util.List;
import lombok.Data;

@Data
public class AddItemData {

  private String url;
  private String title;
  private List<String> tags;
}

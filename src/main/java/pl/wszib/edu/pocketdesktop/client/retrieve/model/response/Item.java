package pl.wszib.edu.pocketdesktop.client.retrieve.model.response;

import java.util.List;
import lombok.Data;

@Data
public class Item {

  private String itemId;
  private String resolvedTitle;
  private String givenUrl;
  private String givenTitle;
  private List<String> tags;
  private Boolean isFavorite;

  public boolean isFavorite() {
    return isFavorite;
  }
}

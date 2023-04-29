package pl.wszib.edu.pocketdesktop.client.retrieve.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ItemsFilterOptions {

  private boolean favorite;
  private List<String> tags;
}

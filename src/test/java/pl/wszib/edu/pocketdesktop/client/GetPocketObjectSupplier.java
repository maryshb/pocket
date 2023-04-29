package pl.wszib.edu.pocketdesktop.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPocketObjectSupplier {

  static Item createItem(String itemId, String resolvedTitle, String givenUrl, String givenTitle, List<String> tags, Boolean isFavorite) {

    Item item = new Item();
    item.setItemId(itemId);
    item.setResolvedTitle(resolvedTitle);
    item.setGivenUrl(givenUrl);
    item.setGivenTitle(givenTitle);
    item.setTags(tags);
    item.setIsFavorite(isFavorite);

    return item;
  }

  static List<String> createTagsList(String... tags) {

    List<String> tagsList = new ArrayList<>();
    Collections.addAll(tagsList, tags);

    return tagsList;
  }
}

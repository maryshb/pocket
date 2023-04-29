package pl.wszib.edu.pocketdesktop.client.retrieve;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.ItemResponse;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.SavedUrlsResponse;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.TagResponse;

@Component
public class RetrieveConverter {

  public List<Item> convertToItemList(SavedUrlsResponse savedUrlsResponse) {

    if (savedUrlsResponse.getList() instanceof Map) {
      Map<String, ItemResponse> mapOfItems = (Map<String, ItemResponse>) savedUrlsResponse.getList();

      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      List<ItemResponse> itemResponseList = mapper.convertValue(mapOfItems.values(),
          new TypeReference<List<ItemResponse>>() {
          });

      return itemResponseList.stream().map(this::convertOneItem)
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  private Item convertOneItem(ItemResponse itemResponse) {

    Item item = new Item();
    item.setItemId(itemResponse.getItem_id());
    item.setResolvedTitle(itemResponse.getResolved_title());
    item.setGivenUrl(itemResponse.getGiven_url());
    item.setGivenTitle(itemResponse.getGiven_title());
    item.setIsFavorite(convertFromBooleanString(itemResponse.getFavorite()));

    setTagList(item, itemResponse);

    return item;
  }

  private void setTagList(Item item, ItemResponse itemResponse) {

    if (itemResponse.getTags() != null) {
      Map<String, TagResponse> tagResponseMap = itemResponse.getTags();
      Collection<TagResponse> tagResponseList = tagResponseMap.values();

      List<String> tagList = tagResponseList.stream().map(this::convertToOneTag)
          .collect(Collectors.toList());

      item.setTags(tagList);
    }
  }

  private String convertToOneTag(TagResponse tagResponse) {

    return tagResponse.getTag();
  }

  private boolean convertFromBooleanString(String booleanFlag) {

    return booleanFlag.equals("1");
  }
}

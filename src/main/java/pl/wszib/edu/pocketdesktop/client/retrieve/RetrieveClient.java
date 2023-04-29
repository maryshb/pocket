package pl.wszib.edu.pocketdesktop.client.retrieve;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.ItemsFilterOptions;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.request.FavoriteItemsRequest;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.request.TaggedItemsRequest;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.SavedUrlsResponse;

@Service
@AllArgsConstructor
public class RetrieveClient {

  private static final String POCKET_URL_GET = "https://getpocket.com/v3/get.php";
  private final RestTemplate restTemplate;
  private final RetrieveConverter converter;
  private final AuthorizedRequestBase authorizedRequestBase;

  public List<Item> retrieveItemList() {

    HttpEntity<AuthorizedRequestBase> entity = new HttpEntity<>(authorizedRequestBase);

    SavedUrlsResponse savedUrlsResponse = restTemplate.postForObject(POCKET_URL_GET, entity,
        SavedUrlsResponse.class);

    return converter.convertToItemList(Objects.requireNonNull(savedUrlsResponse));
  }

  public List<Item> retrieveFavoriteItemList() {

    FavoriteItemsRequest favoriteItemsRequest = new FavoriteItemsRequest(1, authorizedRequestBase);
    HttpEntity<FavoriteItemsRequest> entity = new HttpEntity<>(favoriteItemsRequest);

    SavedUrlsResponse savedUrlsResponse = restTemplate.postForObject(POCKET_URL_GET, entity,
        SavedUrlsResponse.class);

    return converter.convertToItemList(Objects.requireNonNull(savedUrlsResponse));
  }

  public List<Item> retrieveUnTaggedItems() {

    TaggedItemsRequest taggedItemsRequest = new TaggedItemsRequest("_untagged_",
        authorizedRequestBase);
    HttpEntity<TaggedItemsRequest> entity = new HttpEntity<>(taggedItemsRequest);

    SavedUrlsResponse savedUrlsResponse = restTemplate.postForObject(POCKET_URL_GET, entity,
        SavedUrlsResponse.class);

    return converter.convertToItemList(Objects.requireNonNull(savedUrlsResponse));
  }

  public List<Item> retrieveFilteredItemList(ItemsFilterOptions itemsFilterOptions) {

    List<String> chosenTags = itemsFilterOptions.getTags();
    List<Item> filteredList = new ArrayList<>();

    boolean isFavorite = itemsFilterOptions.isFavorite();
    boolean areChosenTagsEmpty = chosenTags.isEmpty();

    if (areChosenTagsEmpty && !isFavorite) {
      filteredList.addAll(retrieveUnTaggedItems());
    } else if (!areChosenTagsEmpty) {
      filteredList.addAll(getItemsWithChosenTags(retrieveItemList(), chosenTags));
    }

    if (isFavorite) {
      recalculateFilteredList(filteredList, areChosenTagsEmpty);
    }

    return filteredList;
  }

  private void recalculateFilteredList(List<Item> filteredList, boolean areChosenTagsEmpty) {

    if (!areChosenTagsEmpty) {
      filteredList.retainAll(retrieveFavoriteItemList());
    }

    if (areChosenTagsEmpty) {
      filteredList.addAll(retrieveFavoriteItemList());
    }
  }

  public List<String> getTagList(List<Item> allSavedItems) {

    return allSavedItems.stream()
        .filter(item -> item.getTags() != null) //
        .flatMap(item -> item.getTags().stream()) //
        .distinct() //
        .collect(Collectors.toList()); //
  }

  private List<Item> getItemsWithChosenTags(List<Item> listOfItems, List<String> chosenTags) {

    return listOfItems.stream()
        .filter(item -> item.getTags() != null)
        .filter(item -> new HashSet<>(item.getTags()).containsAll(chosenTags))
        .collect(Collectors.toList());
  }
}

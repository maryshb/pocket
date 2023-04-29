package pl.wszib.edu.pocketdesktop.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.wszib.edu.pocketdesktop.client.GetPocketObjectSupplier.createItem;
import static pl.wszib.edu.pocketdesktop.client.GetPocketObjectSupplier.createTagsList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.wszib.edu.pocketdesktop.client.retrieve.RetrieveClient;
import pl.wszib.edu.pocketdesktop.client.retrieve.model.response.Item;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RetrieveClientTest {

  @Mock
  private RetrieveClient retrieveClient;

  @BeforeEach
  void setUp() {

    List<Item> itemList = new ArrayList<>();
    Item item1 = createItem("item1", "resolvedTitle1", "http://url1.com", "title1",
        createTagsList("tag1", "tag2"), true);
    Item item2 = createItem("item2", "resolvedTitle2", "http://url2.com", "title2",
        createTagsList("tag1", "tag2"), false);
    Item item3 = createItem("item3", "resolvedTitle3", "http://url3.com", "title3",
        createTagsList(), true);
    Item item4 = createItem("item4", "resolvedTitle4", "http://url4.com", "title4",
        createTagsList(), false);
    Item item5 = createItem("item5", "resolvedTitle5", "http://url5.com", "title5",
        createTagsList("tag2", "tag3", "tag4"), true);

    itemList.add(item1);
    itemList.add(item2);
    itemList.add(item3);
    itemList.add(item4);
    itemList.add(item5);

    List<Item> favoriteItemList = new ArrayList<>();
    favoriteItemList.add(item1);
    favoriteItemList.add(item3);
    favoriteItemList.add(item5);

    List<Item> untaggedItemList = new ArrayList<>();
    untaggedItemList.add(item3);
    untaggedItemList.add(item4);

    Mockito.when(retrieveClient.retrieveItemList()).thenReturn(itemList);
    Mockito.when(retrieveClient.retrieveFavoriteItemList()).thenReturn(favoriteItemList);
    Mockito.when(retrieveClient.retrieveUnTaggedItems()).thenReturn(untaggedItemList);
  }

  @Test
  void retrieveItemList_shouldRetrieveAllItemsAndAssertFields() {

    // when
    List<Item> itemList = retrieveClient.retrieveItemList();

    // then
    assertNotNull(itemList);
    assertEquals(5, itemList.size());

    Item expectedItem = itemList.get(0);
    assertEquals("item1", expectedItem.getItemId());
    assertEquals("resolvedTitle1", expectedItem.getResolvedTitle());
    assertEquals("http://url1.com", expectedItem.getGivenUrl());
    assertEquals("title1", expectedItem.getGivenTitle());
    assertTrue(expectedItem.isFavorite());

    assertNotNull(expectedItem.getTags());
    List<String> expectedTags = expectedItem.getTags();
    assertEquals(2, expectedTags.size());
    assertEquals("tag1", expectedTags.get(0));
    assertEquals("tag2", expectedTags.get(1));
  }

  @Test
  void retrieveFavoriteItemList_shouldRetrieveFavoriteItemsOnly() {

    // when
    List<Item> favoriteItemList = retrieveClient.retrieveFavoriteItemList();

    // then
    assertNotNull(favoriteItemList);
    assertEquals(3, favoriteItemList.size());
  }

  @Test
  void retrieveUnTaggedItemList_shouldGetUntaggedItemsOnly() {

    // when
    List<Item> untaggedItemList = retrieveClient.retrieveUnTaggedItems();

    // then
    assertNotNull(untaggedItemList);
    assertEquals(2, untaggedItemList.size());

    Item expectedItem1 = untaggedItemList.get(0);
    assertEquals("item3", expectedItem1.getItemId());
    assertEquals("resolvedTitle3", expectedItem1.getResolvedTitle());
    assertEquals("http://url3.com", expectedItem1.getGivenUrl());
    assertEquals("title3", expectedItem1.getGivenTitle());
    assertTrue(expectedItem1.isFavorite());

    Item expectedItem2 = untaggedItemList.get(1);
    assertEquals("item4", expectedItem2.getItemId());
    assertEquals("resolvedTitle4", expectedItem2.getResolvedTitle());
    assertEquals("http://url4.com", expectedItem2.getGivenUrl());
    assertEquals("title4", expectedItem2.getGivenTitle());
    assertFalse(expectedItem2.isFavorite());
  }
}

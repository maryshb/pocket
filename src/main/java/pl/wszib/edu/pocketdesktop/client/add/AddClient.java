package pl.wszib.edu.pocketdesktop.client.add;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;
import pl.wszib.edu.pocketdesktop.client.add.model.AddItemData;
import pl.wszib.edu.pocketdesktop.client.add.model.AddItemRequest;
import pl.wszib.edu.pocketdesktop.client.add.model.AddItemResponse;

@Service
@AllArgsConstructor
public class AddClient {

  private static final String POCKET_URL_ADD = "https://getpocket.com/v3/add.php";
  private final RestTemplate restTemplate;
  private final AuthorizedRequestBase authorizedRequestBase;

  public void addItem(AddItemData addItemData) {

    AddItemRequest addItemRequest = new AddItemRequest(authorizedRequestBase,
        addItemData.getUrl(),
        addItemData.getTitle(), addItemData.getTags());

    HttpEntity<AddItemRequest> entity = new HttpEntity<>(addItemRequest);

    restTemplate.postForObject(POCKET_URL_ADD, entity,
        AddItemResponse.class);
  }
}

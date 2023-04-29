package pl.wszib.edu.pocketdesktop.client.modify;

import static pl.wszib.edu.pocketdesktop.client.modify.model.ModifyAction.getActionList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wszib.edu.pocketdesktop.client.AuthorizedRequestBase;
import pl.wszib.edu.pocketdesktop.client.modify.model.ModifyAction;
import pl.wszib.edu.pocketdesktop.client.modify.model.ModifyOption;
import pl.wszib.edu.pocketdesktop.client.modify.model.ModifyRequest;
import pl.wszib.edu.pocketdesktop.client.modify.model.ModifyResponse;
import pl.wszib.edu.pocketdesktop.client.modify.model.tags.DeleteTagAction;
import pl.wszib.edu.pocketdesktop.client.modify.model.tags.RenameTagAction;
import pl.wszib.edu.pocketdesktop.client.modify.model.tags.ReplaceItemTagsAction;

@Service
@AllArgsConstructor
public class ModifyClient {

  private static final String POCKET_URL_EDIT = "https://getpocket.com/v3/send.php";
  private final RestTemplate restTemplate;
  private final AuthorizedRequestBase authorizedRequestBase;

  public void modifyItem(String itemId, ModifyOption option) {

    List<ModifyAction> actions = getActionList(option.get(), itemId);

    ModifyRequest<ModifyAction> modifyItemRequest = new ModifyRequest<>(actions,
        authorizedRequestBase);
    HttpEntity<ModifyRequest<ModifyAction>> entity = new HttpEntity<>(modifyItemRequest);

    restTemplate.postForObject(POCKET_URL_EDIT, entity, ModifyResponse.class);
  }

  public void renameTags(Map<String, String> tagsToRename) {

    List<RenameTagAction> renameTagActionList = getRenameTagActionList(tagsToRename);

    ModifyRequest<RenameTagAction> modifyRequest = new ModifyRequest<>(renameTagActionList,
        authorizedRequestBase);

    HttpEntity<ModifyRequest<RenameTagAction>> entity = new HttpEntity<>(modifyRequest);

    restTemplate.postForObject(POCKET_URL_EDIT, entity, ModifyResponse.class);
  }

  public void deleteTags(List<String> tagsToRemove) {

    List<DeleteTagAction> deleteTagActionList = getDeleteTagActionList(tagsToRemove);

    ModifyRequest<DeleteTagAction> modifyRequest = new ModifyRequest<>(deleteTagActionList,
        authorizedRequestBase);
    HttpEntity<ModifyRequest<DeleteTagAction>> entity = new HttpEntity<>(modifyRequest);

    restTemplate.postForObject(POCKET_URL_EDIT, entity, ModifyResponse.class);
  }

  public void replaceTags(String itemId, List<String> tagsToReplace) {

    List<ReplaceItemTagsAction> replaceItemTagsActionList = getReplaceItemTagsActionAsList(itemId,
        tagsToReplace);

    ModifyRequest<ReplaceItemTagsAction> modifyRequest = new ModifyRequest<>(
        replaceItemTagsActionList, authorizedRequestBase);
    HttpEntity<ModifyRequest<ReplaceItemTagsAction>> entity = new HttpEntity<>(modifyRequest);

    restTemplate.postForObject(POCKET_URL_EDIT, entity, ModifyResponse.class);
  }

  private List<RenameTagAction> getRenameTagActionList(Map<String, String> tagsToRename) {

    return tagsToRename.entrySet().stream()
        .map(entry -> new RenameTagAction(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private List<DeleteTagAction> getDeleteTagActionList(List<String> tagsToRemove) {

    return tagsToRemove.stream().map(DeleteTagAction::new)
        .collect(Collectors.toList());
  }

  private List<ReplaceItemTagsAction> getReplaceItemTagsActionAsList(String itemId,
      List<String> tagsToReplace) {

    List<ReplaceItemTagsAction> replaceItemTagsActionList = new ArrayList<>();
    replaceItemTagsActionList.add(new ReplaceItemTagsAction(itemId, tagsToReplace));
    return replaceItemTagsActionList;
  }
}

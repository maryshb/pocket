package pl.wszib.edu.pocketdesktop.client.retrieve.model.response;

import lombok.Data;

@Data
public class SavedUrlsResponse {

  private int status;
  private int complete;
  private Object list;
  private String error;
  SearchMeta search_meta;
  private int since;
}

package pl.wszib.edu.pocketdesktop.client.add.model;

import lombok.Data;

@Data
public class AddItemResponse {

  private AddItemSingleResponse item;
  private int status;
}

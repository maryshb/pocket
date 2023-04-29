package pl.wszib.edu.pocketdesktop.client.modify.model;

import java.util.List;
import lombok.Data;

@Data
public class ModifyResponse {

  private List<String> action_results;
  private List<String> action_errors;
  private int status;
}

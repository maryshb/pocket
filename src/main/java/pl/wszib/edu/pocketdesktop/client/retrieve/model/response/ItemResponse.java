package pl.wszib.edu.pocketdesktop.client.retrieve.model.response;

import java.util.Map;
import lombok.Data;

@Data
public class ItemResponse {

  private String item_id;
  private String resolved_id;
  private String given_url;
  private String given_title;
  private String favorite;
  private String status;
  private String time_added;
  private String time_updated;
  private String time_read;
  private String time_favorited;
  private int sort_id;
  private String resolved_title;
  private String resolved_url;
  private String excerpt;
  private String is_article;
  private String is_index;
  private String has_video;
  private String has_image;
  private String word_count;
  private String lang;
  private Map<String, TagResponse> tags;
  private int listen_duration_estimate;
}

package pl.wszib.edu.pocketdesktop.client.add.model;

import lombok.Data;

@Data
public class AddItemSingleResponse {

  private String item_id;
  private String normal_url;
  private String resolved_id;
  private String extended_item_id;
  private String resolved_url;
  private String domain_id;
  private String origin_domain_id;
  private String response_code;
  private String mime_type;
  private String content_length;
  private String encoding;
  private String date_resolved;
  private String date_published;
  private String title;
  private String excerpt;
  private String word_count;
  private String innerdomain_redirect;
  private String login_required;
  private String has_image;
  private String has_video;
  private String is_index;
  private String is_article;
  private String used_fallback;
  private String lang;
  private String time_first_parsed;
  private String given_url;
}

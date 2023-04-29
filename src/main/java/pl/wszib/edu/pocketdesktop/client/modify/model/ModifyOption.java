package pl.wszib.edu.pocketdesktop.client.modify.model;

public enum ModifyOption {

  ARCHIVE, FAVORITE, UNFAVORITE, DELETE;

  public String get() {
    return this.toString().toLowerCase();
  }
}

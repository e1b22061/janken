package oit.is.z2620.kaizi.janken.model;

import org.springframework.stereotype.Component;

@Component
public class User {
  int id;
  String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

package oit.is.z2620.kaizi.janken.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class Entry {
  ArrayList<String> entries = new ArrayList<>();
  int roomNo = 1;

  public void addEntry(String name) {
    for (String s : this.entries) {
      if (s.equals(name)) {
        return;
      }
    }
    this.entries.add(name);
  }

  public int getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(int roomNo) {
    this.roomNo = roomNo;
  }

  public ArrayList<String> getEntries() {
    return entries;
  }

  public void setEntries(ArrayList<String> users) {
    this.entries = users;
  }

}

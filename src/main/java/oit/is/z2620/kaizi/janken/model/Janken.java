package oit.is.z2620.kaizi.janken.model;

import java.util.HashMap;
import java.util.Map;

public class Janken {
  public Map<String, String> judge(String playerHand) {
    String cpuHand = "r";
    String result = "";
    Map<String, String> outcome = new HashMap<>();
    if (playerHand != null) {
      if (playerHand.equals(cpuHand)) {
        result = "引き分け";
      } else if (playerHand.equals("s")) {
        result = "負け";
      } else if (playerHand.equals("p")) {
        result = "勝ち";
      } else {
        result = "Error";
      }
      outcome.put("playerHand", translateHand(playerHand));
      outcome.put("cpuHand", translateHand(cpuHand));
      outcome.put("result", result);
    }
    return outcome;
  }

  private String translateHand(String hand) {
    if (hand.equals("r")) {
      return "グー";
    } else if (hand.equals("s")) {
      return "チョキ";
    } else if (hand.equals("p")) {
      return "パー";
    } else {
      return "Invalid hand";
    }
  }
}

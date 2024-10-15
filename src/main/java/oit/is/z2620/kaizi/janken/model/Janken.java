package oit.is.z2620.kaizi.janken.model;

import java.util.HashMap;
import java.util.Map;

public class Janken {
  private String[] hands = { "Gu", "Choki", "Pa" };

  public Map<String, String> judge(String playerHand) {
    String opponentHand = getCpuHand();
    String result = "";
    Map<String, String> outcome = new HashMap<>();
    if (playerHand != null) {
      result = judge(playerHand, opponentHand);
      // outcome.put("playerHand", translateHand(playerHand));
      // outcome.put("cpuHand", translateHand(cpuHand));
      outcome.put("playerHand", playerHand);
      outcome.put("opponentHand", opponentHand);
      outcome.put("result", result);
    }
    return outcome;
  }

  private String getCpuHand() {
    return "Gu";
  }

  private String judge(String playerHand, String opponentHand) {
    if (playerHand.equals(opponentHand)) {
      return "Draw";
    }
    if (playerHand.equals(hands[0])) {
      return (opponentHand.equals(hands[1]) ? "Win" : "Lose");
    } else if (playerHand.equals(hands[1])) {
      return (opponentHand.equals(hands[2]) ? "Win" : "Lose");
    } else {
      return (opponentHand.equals(hands[0]) ? "Win" : "Lose");
    }
  }

  // private String translateHand(String hand) {
  // if (hand.equals("Gu")) {
  // return "グー";
  // } else if (hand.equals("Choki")) {
  // return "チョキ";
  // } else if (hand.equals("Pa")) {
  // return "パー";
  // } else {
  // return "Invalid hand";
  // }
  // }
}

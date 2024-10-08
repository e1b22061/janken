package oit.is.z2620.kaizi.janken.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import oit.is.z2620.kaizi.janken.model.Janken;
import oit.is.z2620.kaizi.janken.model.Entry;

@Controller
public class JankenController {
  @Autowired
  private Entry entry;

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/janken")
  public String playJanken(@RequestParam(value = "playerHand", required = false) String playerHand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addEntry(loginUser);
    model.addAttribute("name", loginUser);
    model.addAttribute("entry", this.entry);

    Janken janken = new Janken();
    Map<String, String> outcome = janken.judge(playerHand);

    model.addAttribute("playerHand", outcome.get("playerHand"));
    model.addAttribute("cpuHand", outcome.get("cpuHand"));
    model.addAttribute("result", outcome.get("result"));
    return "janken.html"; // 結果を表示するページに遷移
  }
}

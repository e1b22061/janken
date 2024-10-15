package oit.is.z2620.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import oit.is.z2620.kaizi.janken.model.Janken;
import oit.is.z2620.kaizi.janken.model.User;
import oit.is.z2620.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {
  @Autowired
  private UserMapper um;

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/janken")
  public String playJanken(@RequestParam(value = "playerHand", required = false) String playerHand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    User newUser = new User();
    newUser.setName(loginUser);
    try {
      this.um.insertUser(newUser);
    } catch (RuntimeException e) {
      System.out.println("Exception:" + e.getMessage());
    }
    ArrayList<User> users = this.um.selectAllUser();
    model.addAttribute("users", users);

    Janken janken = new Janken();
    Map<String, String> outcome = janken.judge(playerHand);

    model.addAttribute("playerHand", outcome.get("playerHand"));
    model.addAttribute("cpuHand", outcome.get("cpuHand"));
    model.addAttribute("result", outcome.get("result"));
    return "janken.html"; // 結果を表示するページに遷移
  }
}

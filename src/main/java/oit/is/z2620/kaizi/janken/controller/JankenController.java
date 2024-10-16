package oit.is.z2620.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import oit.is.z2620.kaizi.janken.model.Janken;
import oit.is.z2620.kaizi.janken.model.User;
import oit.is.z2620.kaizi.janken.model.UserMapper;
import oit.is.z2620.kaizi.janken.model.Match;
import oit.is.z2620.kaizi.janken.model.MatchMapper;

@Controller
public class JankenController {
  @Autowired
  private UserMapper um;
  @Autowired
  private MatchMapper mm;

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/janken")
  @Transactional
  public String playJanken(Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("user", loginUser);
    User newUser = new User();
    newUser.setName(loginUser);
    boolean checkDuplicate = false;
    for (User user : this.um.selectAllUser()) {
      if (user.getName().equals(loginUser)) {
        checkDuplicate = true;
      }
    }
    if (checkDuplicate == false) {
      this.um.insertUser(newUser);
    }
    ArrayList<User> users = this.um.selectAllUser();
    model.addAttribute("users", users);
    ArrayList<Match> matches = this.mm.selectAllMatch();
    model.addAttribute("matches", matches);

    return "janken.html"; // 結果を表示するページに遷移
  }

  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    String player = prin.getName();
    model.addAttribute("player", player);
    User opponent = this.um.selectById(id);
    model.addAttribute("opponent", opponent.getName());
    model.addAttribute("opponentId", id);

    return "match.html";
  }

  @GetMapping("/fight")
  @Transactional
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    Janken janken = new Janken();
    Map<String, String> outcome = janken.judge(hand);

    User player = this.um.selectByName(prin.getName());
    model.addAttribute("player", player.getName());
    User opponent = this.um.selectById(id);
    model.addAttribute("opponent", opponent.getName());
    model.addAttribute("opponentId", id);
    model.addAttribute("playerHand", outcome.get("playerHand"));
    model.addAttribute("cpuHand", outcome.get("opponentHand"));
    model.addAttribute("result", outcome.get("result"));

    Match newMatch = new Match();
    newMatch.setUser1(player.getId());
    newMatch.setUser2(id);
    newMatch.setUser1Hand(hand);
    newMatch.setUser2Hand(outcome.get("opponentHand"));
    this.mm.insertMatch(newMatch);
    return "match.html";
  }
}

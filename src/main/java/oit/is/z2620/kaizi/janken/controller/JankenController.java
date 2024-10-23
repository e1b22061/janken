package oit.is.z2620.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

import oit.is.z2620.kaizi.janken.model.User;
import oit.is.z2620.kaizi.janken.model.UserMapper;
import oit.is.z2620.kaizi.janken.model.Match;
import oit.is.z2620.kaizi.janken.model.MatchInfo;
import oit.is.z2620.kaizi.janken.model.MatchMapper;
import oit.is.z2620.kaizi.janken.model.MatchInfoMapper;
import oit.is.z2620.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {
  @Autowired
  private UserMapper um;
  @Autowired
  private MatchMapper mm;
  @Autowired
  private MatchInfoMapper mim;

  @Autowired
  private AsyncKekka asynckekka;

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
    ArrayList<MatchInfo> activeMatchInfos = this.mim.selectActiveMatchInfo();
    if (activeMatchInfos.size() > 0) {
      model.addAttribute("active_matches", activeMatchInfos);
    }
    ArrayList<Match> matches = this.mm.selectAllMatch();
    model.addAttribute("matches", matches);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    User player = this.um.selectByName(prin.getName());
    model.addAttribute("player", player);
    User opponent = this.um.selectById(id);
    model.addAttribute("opponent", opponent);

    return "match.html";
  }

  @GetMapping("/fight")
  @Transactional
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    User player = this.um.selectByName(prin.getName());
    model.addAttribute("player", player);
    User opponent = this.um.selectById(id);
    model.addAttribute("opponent", opponent);
    model.addAttribute("hand", hand);

    ArrayList<MatchInfo> activeMatchInfos = this.mim.selectActiveMatchInfoById(opponent.getId(), player.getId());
    if (activeMatchInfos.size() == 0) {
      MatchInfo newMatchInfo = new MatchInfo();
      newMatchInfo.setUser1(player.getId());
      newMatchInfo.setUser2(opponent.getId());
      newMatchInfo.setUser1Hand(hand);
      newMatchInfo.setActive(true);
      this.mim.insertMatchInfo(newMatchInfo);
    } else {
      MatchInfo activeMatchInfo = activeMatchInfos.get(0);
      Match newMatch = new Match();
      newMatch.setUser1(activeMatchInfo.getUser1());
      newMatch.setUser2(player.getId());
      newMatch.setUser1Hand(activeMatchInfo.getUser1Hand());
      newMatch.setUser2Hand(hand);
      newMatch.setActive(true);
      this.asynckekka.insertMatch(newMatch);
      this.mim.updateMatchInfo(activeMatchInfo.getId());
    }
    return "wait.html";
  }

  @GetMapping("/result")
  public SseEmitter pushResult() {
    final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    try {
      this.asynckekka.getResult(emitter);
    } catch (IOException e) {
      System.err.println(e);
      emitter.complete();
    }
    return emitter;
  }
}

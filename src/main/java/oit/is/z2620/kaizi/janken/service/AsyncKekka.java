package oit.is.z2620.kaizi.janken.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2620.kaizi.janken.model.MatchMapper;
import oit.is.z2620.kaizi.janken.model.Match;

@Service
public class AsyncKekka {
  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);
  boolean dbUpdated = false;

  @Autowired
  MatchMapper mm;

  @Transactional
  public void insertMatch(Match m) {
    this.mm.insertMatch(m);
    this.dbUpdated = true;
    logger.info("insertMatch complete");
  }

  @Async
  public void getResult(SseEmitter emitter) throws IOException {
    try {
      while (true) {
        if (!dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<Match> activeMatches = this.mm.selectActiveMatch();
        if (activeMatches.size() > 0) {
          emitter.send(formatResult(activeMatches.get(0)));
          TimeUnit.MILLISECONDS.sleep(1000);
          this.mm.updateMatch(activeMatches.get(0));
          this.dbUpdated = false;
        }
      }
    } catch (Exception e) {
      logger.error("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    logger.info("asynckekka complete");
  }

  private String formatResult(Match m) {
    String result = "id: " + m.getId() + " user1: " + m.getUser1() + " user2: " + m.getUser2() + " user1Hand: "
        + m.getUser1Hand() + " user2Hand: " + m.getUser2Hand() + " isActive: " + m.isActive();
    return result;
  }
}

package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

  private final MatchService matchService;

  @Autowired
  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  @PostMapping(consumes = "text/plain")
  public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
    final Long matchId = matchService.ingestMatch(payload);
    return ResponseEntity.ok(matchId);
  }

  @GetMapping("{matchId}")
  public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
    return ResponseEntity.ok(
        matchService.fetchHeroesKillCounts(matchId).stream()
            .map(m -> new HeroKills((String) m.get("hero"), (Integer) m.get("kills")))
            .collect(Collectors.toList()));
  }

  @GetMapping("{matchId}/{heroName}/items")
  public ResponseEntity<List<HeroItems>> getItems(
      @PathVariable("matchId") Long matchId, @PathVariable("heroName") String heroName) {

    return ResponseEntity.ok(
        matchService.fetchHeroItems(matchId, heroName).stream()
            .map(i -> new HeroItems(i.getItem(), i.getTimestamp()))
            .collect(Collectors.toList()));
  }

  @GetMapping("{matchId}/{heroName}/spells")
  public ResponseEntity<List<HeroSpells>> getSpells(
      @PathVariable("matchId") Long matchId, @PathVariable("heroName") String heroName) {

    return ResponseEntity.ok(
        matchService.fetchHeroSpellsCasts(matchId, heroName).stream()
            .map(m -> new HeroSpells((String) m.get("spell"), (Integer) m.get("casts")))
            .collect(Collectors.toList()));
  }

  @GetMapping("{matchId}/{heroName}/damage")
  public ResponseEntity<List<HeroDamage>> getDamage(
      @PathVariable("matchId") Long matchId, @PathVariable("heroName") String heroName) {

    return ResponseEntity.ok(
        matchService.fetchHeroDamageDone(matchId, heroName).stream()
            .map(
                m ->
                    new HeroDamage(
                        (String) m.get("target"),
                        (Integer) m.get("instances"),
                        (Integer) m.get("totalDamage")))
            .collect(Collectors.toList()));
  }
}

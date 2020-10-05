package gg.bayes.challenge.repository;

import gg.bayes.challenge.repository.entity.MatchEvent;
import gg.bayes.challenge.repository.entity.PurchaseItemEvent;
import java.util.List;
import java.util.Map;

public interface MatchRepository {

  void saveEvent(MatchEvent event);

  List<Map<String, Object>> countHeroesKills(Long matchId);

  List<Map<String, Object>> countHeroSpellsCasts(Long matchId, String hero);

  List<Map<String, Object>> findHeroHits(Long matchId, String hero);

  List<PurchaseItemEvent> findHeroItems(Long matchId, String hero);
}

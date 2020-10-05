package gg.bayes.challenge.repository.impl;

import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.repository.entity.MatchEvent;
import gg.bayes.challenge.repository.entity.PurchaseItemEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MatchRepositoryImpl implements MatchRepository {

  private final EntityManager em;

  @Autowired
  public MatchRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public void saveEvent(MatchEvent event) {
    em.persist(event);
  }

  @Override
  public List<Map<String, Object>> countHeroesKills(Long matchId) {
    return em.createNamedQuery("KillHeroEvent.countKills", Tuple.class)
        .setParameter("matchId", matchId)
        .getResultStream()
        .map(t -> Map.of("hero", t.get(0), "kills", ((Number) t.get(1)).intValue()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Map<String, Object>> countHeroSpellsCasts(Long matchId, String hero) {
    return em.createNamedQuery("CastSpellEvent.countCasts", Tuple.class)
        .setParameter("matchId", matchId)
        .setParameter("hero", hero)
        .getResultStream()
        .map(t -> Map.of("spell", t.get(0), "casts", ((Number) t.get(1)).intValue()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Map<String, Object>> findHeroHits(Long matchId, String hero) {
    return em.createNamedQuery("DamageHeroEvent.findHeroHits", Tuple.class)
        .setParameter("matchId", matchId)
        .setParameter("hero", hero)
        .getResultStream()
        .map(
            t ->
                Map.of(
                    "target",
                    t.get(0),
                    "instances",
                    ((Number) t.get(1)).intValue(),
                    "totalDamage",
                    ((Number) t.get(2)).intValue()))
        .collect(Collectors.toList());
  }

  @Override
  public List<PurchaseItemEvent> findHeroItems(Long matchId, String hero) {
    return em.createNamedQuery("PurchaseItemEvent.findHeroItems", PurchaseItemEvent.class)
        .setParameter("matchId", matchId)
        .setParameter("hero", hero)
        .getResultList();
  }
}

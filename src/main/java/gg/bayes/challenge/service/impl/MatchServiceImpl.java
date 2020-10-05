package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.repository.entity.PurchaseItemEvent;
import gg.bayes.challenge.service.MatchEventParser;
import gg.bayes.challenge.service.MatchService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;

  private final AtomicLong matchIdGenerator = new AtomicLong();

  @Autowired
  public MatchServiceImpl(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  @Override
  @Transactional
  public Long ingestMatch(String payload) {
    Long matchId = matchIdGenerator.incrementAndGet();

    payload
        .lines()
        .forEach(
            line -> {
              Arrays.stream(MatchEventParser.values())
                  .map(parser -> parser.parseEvent(matchId, line))
                  .filter(e -> e != null)
                  .findFirst()
                  .ifPresent(e -> matchRepository.saveEvent(e));
            });

    return matchId;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Map<String, Object>> fetchHeroesKillCounts(Long matchId) {
    return matchRepository.countHeroesKills(matchId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Map<String, Object>> fetchHeroSpellsCasts(Long matchId, String hero) {
    return matchRepository.countHeroSpellsCasts(matchId, hero);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Map<String, Object>> fetchHeroDamageDone(Long matchId, String hero) {
    return matchRepository.findHeroHits(matchId, hero);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PurchaseItemEvent> fetchHeroItems(Long matchId, String hero) {
    return matchRepository.findHeroItems(matchId, hero);
  }
}

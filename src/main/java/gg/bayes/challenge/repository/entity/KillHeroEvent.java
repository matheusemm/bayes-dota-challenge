package gg.bayes.challenge.repository.entity;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_kill_hero")
@NamedNativeQueries({
  @NamedNativeQuery(
      name = "KillHeroEvent.countKills",
      query =
          "SELECT "
              + "   match_event.hero, "
              + "   COUNT(event_kill_hero.id) "
              + "FROM "
              + "   match_event "
              + "   LEFT JOIN event_kill_hero ON event_kill_hero.id = match_event.id "
              + "WHERE "
              + "   match_event.match_id = :matchId "
              + "GROUP BY "
              + "   match_event.hero ")
})
@NoArgsConstructor
@Getter
@Setter
public class KillHeroEvent extends MatchEvent {

  @NotEmpty private String killed;

  public KillHeroEvent(Long matchId, Long timestamp, String hero, String killed) {
    super(matchId, timestamp, hero);
    this.killed = killed;
  }
}

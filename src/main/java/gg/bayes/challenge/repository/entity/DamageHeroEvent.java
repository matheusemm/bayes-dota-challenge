package gg.bayes.challenge.repository.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_damage_hero")
@NamedQueries({
  @NamedQuery(
      name = "DamageHeroEvent.findHeroHits",
      query =
          "SELECT "
              + "   dhe.target, "
              + "   COUNT(dhe.id), "
              + "   SUM(dhe.damage) "
              + "FROM "
              + "   DamageHeroEvent dhe "
              + "WHERE "
              + "   dhe.matchId = :matchId "
              + "   AND dhe.hero = :hero "
              + "GROUP BY "
              + "   dhe.target")
})
@NoArgsConstructor
@Getter
@Setter
public class DamageHeroEvent extends MatchEvent {

  @NotEmpty public String target;

  @NotEmpty public String weapon;

  @NotNull @Positive public Integer damage;

  public DamageHeroEvent(
      Long matchId, Long timestamp, String hero, String target, String weapon, Integer damage) {

    super(matchId, timestamp, hero);
    this.target = target;
    this.weapon = weapon;
    this.damage = damage;
  }
}

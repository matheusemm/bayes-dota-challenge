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
@Table(name = "event_cast_spell")
@NamedQueries({
  @NamedQuery(
      name = "CastSpellEvent.countCasts",
      query =
          "SELECT "
              + "   cse.spell, "
              + "   COUNT(cse.id) "
              + "FROM "
              + "   CastSpellEvent cse "
              + "WHERE "
              + "   cse.matchId = :matchId "
              + "   AND cse.hero = :hero "
              + "GROUP BY "
              + "   cse.spell ")
})
@NoArgsConstructor
@Getter
@Setter
public class CastSpellEvent extends MatchEvent {

  @NotEmpty private String target;

  @NotEmpty private String spell;

  @NotNull @Positive private Integer spellLevel;

  public CastSpellEvent(
      Long matchId, Long timestamp, String hero, String target, String spell, Integer spellLevel) {

    super(matchId, timestamp, hero);
    this.target = target;
    this.spell = spell;
    this.spellLevel = spellLevel;
  }
}

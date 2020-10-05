package gg.bayes.challenge.repository.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_purchase_item")
@NamedQueries({
    @NamedQuery(name = "PurchaseItemEvent.findHeroItems"
    , query = "SELECT "
        + "   pie "
        + "FROM "
        + "   PurchaseItemEvent pie "
        + "WHERE "
        + "   pie.matchId = :matchId "
        + "   and pie.hero = :hero ")
}
)
@NoArgsConstructor
@Getter
@Setter
public class PurchaseItemEvent extends MatchEvent {

  @NotEmpty private String item;

  public PurchaseItemEvent(Long matchId, Long timestamp, String hero, String item) {
    super(matchId, timestamp, hero);
    this.item = item;
  }
}

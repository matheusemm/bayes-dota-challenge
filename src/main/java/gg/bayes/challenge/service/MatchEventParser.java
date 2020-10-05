package gg.bayes.challenge.service;

import gg.bayes.challenge.repository.entity.CastSpellEvent;
import gg.bayes.challenge.repository.entity.DamageHeroEvent;
import gg.bayes.challenge.repository.entity.KillHeroEvent;
import gg.bayes.challenge.repository.entity.MatchEvent;
import gg.bayes.challenge.repository.entity.PurchaseItemEvent;
import java.time.Duration;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MatchEventParser {
  PURCHASE_ITEM("^\\[(.*)\\] npc_dota_hero_(.*) buys item item_(.*)$") {
    @Override
    protected PurchaseItemEvent createEvent(Long matchId, Matcher matcher) {
      return new PurchaseItemEvent(
          matchId,
          PURCHASE_ITEM.parseTimestamp(matcher.group(1)),
          matcher.group(2),
          matcher.group(3));
    }
  },

  KILL_HERO("^\\[(.*)\\] npc_dota_hero_(.*) is killed by npc_dota_hero_(.*)") {
    @Override
    protected KillHeroEvent createEvent(Long matchId, Matcher matcher) {
      return new KillHeroEvent(
          matchId, KILL_HERO.parseTimestamp(matcher.group(1)), matcher.group(3), matcher.group(2));
    }
  },

  DAMAGE_HERO(
      "^\\[(.*)\\] npc_dota_hero_(.*) hits npc_dota_hero_(.*) with (.*) for (\\d+) damage.*$") {
    @Override
    protected DamageHeroEvent createEvent(Long matchId, Matcher matcher) {
      return new DamageHeroEvent(
          matchId,
          DAMAGE_HERO.parseTimestamp(matcher.group(1)),
          matcher.group(2),
          matcher.group(3),
          matcher.group(4),
          Integer.parseInt(matcher.group(5)));
    }
  },

  CAST_SPELL("^\\[(.*)\\] npc_dota_hero_(.*) casts ability (.*) \\(lvl (\\d+)\\) on (.*)$") {
    @Override
    protected CastSpellEvent createEvent(Long matchId, Matcher matcher) {
      String target;
      if (matcher.group(5).startsWith("npc_dota_hero_")) {
        target = matcher.group(5).substring(14);
      } else if (matcher.group(5).startsWith("npc_dota_")) {
        target = matcher.group(5).substring(9);
      } else {
        target = matcher.group(5);
      }

      return new CastSpellEvent(
          matchId,
          CAST_SPELL.parseTimestamp(matcher.group(1)),
          matcher.group(2),
          target,
          matcher.group(3),
          Integer.parseInt(matcher.group(4)));
    }
  };

  private final Pattern pattern;

  MatchEventParser(String regex) {
    this.pattern = Pattern.compile(regex);
  }

  protected abstract MatchEvent createEvent(Long matchId, Matcher matcher);

  public MatchEvent parseEvent(Long matchId, String s) {
    Matcher matcher = pattern.matcher(s);
    return matcher.matches() ? createEvent(matchId, matcher) : null;
  }

  private Long parseTimestamp(String s) {
    return Duration.between(LocalTime.MIDNIGHT, LocalTime.parse(s)).toMillis();
  }
}

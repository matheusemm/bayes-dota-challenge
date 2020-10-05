package gg.bayes.challenge.service;

import static gg.bayes.challenge.service.MatchEventParser.PURCHASE_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import gg.bayes.challenge.repository.entity.PurchaseItemEvent;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MatchEventParserTest {

  @Nested
  @TestInstance(Lifecycle.PER_CLASS)
  @DisplayName("PURCHASE_ITEM parser should...")
  public class PurchaseItemTests {

    @ParameterizedTest
    @MethodSource("successfullyParsePurchaseEventsSource")
    public void successfullyParsePurchaseEvents(
        String line, Long timestamp, String hero, String item) {

      PurchaseItemEvent pie = (PurchaseItemEvent) PURCHASE_ITEM.parseEvent(1L, line);

      assertEquals(pie.getTimestamp(), timestamp);
      assertEquals(pie.getHero(), hero);
      assertEquals(pie.getItem(), item);
    }

    public Stream<Arguments> successfullyParsePurchaseEventsSource() {
      return Stream.of(
          Arguments.of(
              "[00:08:46.693] npc_dota_hero_snapfire buys item item_clarity",
              526693L,
              "snapfire",
              "clarity"),
          Arguments.of(
              "[00:08:46.759] npc_dota_hero_dragon_knight buys item item_quelling_blade",
              526759L,
              "dragon_knight",
              "quelling_blade"),
          Arguments.of(
              "[00:08:48.159] npc_dota_hero_puck buys item item_tango", 528159L, "puck", "tango"),
          Arguments.of(
              "[00:08:52.025] npc_dota_hero_mars buys item item_flask", 532025L, "mars", "flask"),
          Arguments.of(
              "[00:08:52.791] npc_dota_hero_rubick buys item item_enchanted_mango",
              532791L,
              "rubick",
              "enchanted_mango"),
          Arguments.of(
              "[00:09:01.756] npc_dota_hero_bloodseeker buys item item_branches",
              541756L,
              "bloodseeker",
              "branches"));
    }

    @Test
    public void doesntParseOtherTypesOfEvents() {
      String events =
          "[00:00:04.999] game state is now 2\n"
              + "[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown\n"
              + "[00:09:11.953] npc_dota_hero_abyssal_underlord uses item_quelling_blade\n"
              + "[00:10:41.998] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 1) on dota_unknown\n"
              + "[00:10:42.031] npc_dota_hero_bane hits npc_dota_hero_abyssal_underlord with dota_unknown for 51 damage (740->689)\n"
              + "[00:11:02.859] npc_dota_hero_rubick uses item_tango\n"
              + "[00:12:15.108] npc_dota_neutral_harpy_scout is killed by npc_dota_hero_pangolier";

      events.lines().forEach(line -> assertNull(PURCHASE_ITEM.parseEvent(1L, line)));
    }
  }

}

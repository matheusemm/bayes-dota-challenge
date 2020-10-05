# bayes-dota - Coding Challenge Solution

## Assumptions

* Timestamp unit is millisecond.
* All heroes in the log start with `npc_dota_hero_`. This is important because there are kill events where the character killed is not a `npc_dota_hero_` (e.g. `npc_dota_neutral_harpy_scout`); these events are not extracted and stored.

## Decisions

* `MatchEventParserTest.java` contains just enough test code to exemplify how to test the event parsing logic.
* Entities mapping uses `InheritanceType.JOINED` strategy. This will create a table for each type of event of interest (cast spell, damage hero, kill hero, purchase item) and one for the general "match event" to store the common properties (match id, timestamp, hero). You can read about different strategies [here](https://thorben-janssen.com/complete-guide-inheritance-strategies-jpa-hibernate/#Joined). I believe that `JOINED` is a good strategy to avoid duplication of common properties and to allow polymorphic queries.
* I didn't write code for exception handling, response handling (e.g.: translate exceptions to proper HTTP responses), log, monitoring. These are all important aspects to take into account to put a service in production.
* My main focus was code clarity and organization. Particularly I didn't focus on performance, althoug I understant that it can be important in cases where the data to ingest is huge and the processing has to be performed under defined thresholds.
* I avoided adding more classes then necessary, specifically for the values returned from the repository methods. Instead of returning `List<Map<String, Object>>` I could have moved `rest.model` classes to another package and use these classes as DTOs to transport values between layers. That could be a strategy to avoid unecessary data transformation (e.g.: `Tuple` &rarr; `Map`&rarr; model).
* I added lombok's `@AllArgsConstructor` annotation to `rest.model` classes to make it easier to instantiate their objects (e.g.: in the controller methods).
* I added properties to `application.yml` to facilitate the development: `spring.jpa.show-sql: true` and `spring.jpa.properties.hibernate.format_sql: true`.
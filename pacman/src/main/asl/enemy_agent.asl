// enemy_agent.asl

!start.

/* Plans */

+!start : true <-
    .print("Ciao siamo i nemici!!");
    !init_all_enemies.

+!init_all_enemies <-
    .print("Initializing all enemies.");
    !init_enemies(0).
    !init_enemies(1).
    !init_enemies(2).
    !init_enemies(3).

+!init_enemies(Id) <-
    .print("[ASL] Updating enemy: ", Id);
    +enemy_position(Id, 0, 0);  // Set initial position (to be updated)
    .wait(1000);  // Wait for a second
    move_enemy(Id, random);
    !init_enemies(Id).




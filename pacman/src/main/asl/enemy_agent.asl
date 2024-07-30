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
    .print("Initializing enemy ", Id);
    !move_enemy(Id).

+!move_enemy(Id) <-
    .print("Enemy ", Id, " attempting to move randomly");
    move_enemy(Id, random);  // Questo viene letto dall'environment nelle action.
    .wait(1000);  // Wait for a second
    !move_enemy(Id).  // Loop per il movimento continuo

+enemy_position(EnemyIndex, X, Y) : true <-
    .print("Enemy ", EnemyIndex, " si trova in posizione: (", X, ", ", Y, ")").



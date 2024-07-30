// enemy_agent.asl

!start.

/* Plans */

+!start : true <-
    .print("Ciao siamo i nemici!!");
    !move_enemies(random).

+!move_enemies(random)[source(self)] <-
    .print("Enemies are attempting to move randomly");
    move_enemies(random);  // Questo viene letto dall'environment nelle action.
    .wait(1000);  // Wait for a second
    !move_enemies(random).

+!move_enemies(Direction) <-  // Handle move with specified direction
    .print("Moving enemies in direction: ", Direction);
    move_enemies(Direction);
    .wait(1000);  // Wait for a second
    !move_enemies(random).

+enemy_position(EnemyIndex, X, Y) : true <-  // Questo viene invocato da java (nelle percezioni).
    .print("Enemy ", EnemyIndex, " si trova in posizione: (", X, ", ", Y, ")").



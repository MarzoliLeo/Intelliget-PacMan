/* Initial goals */
!start.

/* Plans */

+!start : true <-
    .print("Ciao sono pacman!!");
    !move(preferential).

+!move(preferential)[source(self)] <-
    .print("Attempting to move preferentially");
    move(preferential); // Questo viene letto dall'environment nelle action.(immaginalo come una send all'env).
    .wait(1000);  // Wait for a second
    !move(preferential).

+!move(Direction) <-  // Handle move with specified direction
    .print("Moving in direction: ", Direction);
    move(Direction);
    .wait(1000);  // Wait for a second
    !move(preferential).

+position(X, Y) : true <- // Questo viene invocato da java (nelle percezioni) poiché non ha il !. È un belief in "ascolto".
    .print("Pacman si trova in posizione: (", X, ", ", Y, ")").



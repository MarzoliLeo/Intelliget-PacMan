/* Initial goals */
!start.

/* Plans */

+!start : true <-
    .print("Ciao sono pacman!!");
    !move(random).

+!move(random)[source(self)] <-
    .print("Attempting to move randomly");
    move(random); // Questo viene letto dall'environment nelle action.(immaginalo come una send all'env).
    .wait(1000);  // Wait for a second
    !chooseDirection(NewDirection);
    !move(NewDirection, random).

+!move(Direction, random) <-  // Handle move with random direction
    .print("Moving in direction: ", Direction);
    move(Direction);
    .wait(1000);  // Wait for a second
    !chooseDirection(NewDirection); //crea il loop.
    !move(NewDirection, random). //crea il loop.

+!move(random) <-  // Handle failure
    .print("Failed to move randomly");
    .fail.

+!chooseDirection(Direction) : true <-
    Direction = random.

+position(X, Y) : true <- // Questo viene invocato da java (nelle percezioni) poiché non ha il !. È un belief in "ascolto".
    .print("Pacman si trova in posizione: (", X, ", ", Y, ")").



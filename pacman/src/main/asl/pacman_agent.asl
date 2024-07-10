/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
 .print("Ciao sono pacman!!"),
 move(random).

+!move(Direction) : true <-
 .send(env, tell, Literal.parseLiteral("move(" + Direction + ")")).
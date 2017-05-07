# README #

 * GameStateCreator is used to identify the underlying graph of a game scenario using predefined actions that have pre conditions that should meet and post conditions that will be changed in the game state. 
 * Current development is for room escape games.

## Notes ##
 Loop detection cuts start -> home -> start -> home to  start -> home  should post process prune it also
 as start -> home -> start -> any other action does not cause loops.
 Why should we force start -> home -> start -> home cut to start -> home -> start
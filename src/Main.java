import EscapeGame.*;
import ExampleEscapeGame.Available;
import ExampleEscapeGame.GraphActions;
import Generator.GameGraphGenerator;

import java.util.ArrayList;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
class Main {

    public static void main(String[] args){
        example ();
    }


    private static void example (){
        //initialize state binarization with all of the available game conditions
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( Available.getAvailableActions());
        stateBinarization.addItems( Available.getFirstLevelItems());
        stateBinarization.addRooms( Available.getFistLevelRooms());
        stateBinarization.addSubRooms( Available.getFirstLevelSubRooms());
        stateBinarization.generate ();

        ArrayList<UserAction> userActions = GraphActions.levelOneActions();

        BinarizedGameState menuState = new BinarizedGameState( BinarizedGameState.fromMenu(stateBinarization));
        GameGraphGenerator gameGraphGenerator = new GameGraphGenerator( menuState, stateBinarization, userActions);

        gameGraphGenerator.generate();
        gameGraphGenerator.print();
    }





}
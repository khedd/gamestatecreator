import EscapeGame.*;
import ExampleEscapeGame.ActionFactory;
import ExampleEscapeGame.Available;
import ExampleEscapeGame.Available.Levels;
import Generator.GameGraphGenerator;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
class Main {

    /**
     * Main method calls {@link #initializeGraphActions(StateBinarization)}
     * @param args Not used
     */
    public static void main(String[] args){
        example ();
    }

    private static ArrayList<String> getRooms (){
        ArrayList<String> rooms = new ArrayList<>();
        for ( Levels.Main option:  Available.Levels.Main.values()){
            rooms.add( option.name());
        }
        return rooms;
    }
    private static ArrayList<String> getActions (){
        ArrayList<String> actions = new ArrayList<>();
        for ( Available.Actions option:  Available.Actions.values()){
            actions.add( option.name());
        }
        return actions;
    }
    private static ArrayList<String> getSubRooms (){
        ArrayList<String> subRooms = new ArrayList<>();
        for ( Levels.First option:  Available.Levels.First.values()){
            subRooms.add( option.name());
        }
        return subRooms;
    }
    private static ArrayList<String> getItems() {
        ArrayList<String> items = new ArrayList<>();
        for ( Available.Items.First item:  Available.Items.First.values()){
            items.add( item.name());
        }
        return items;
    }

    private static void example (){
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        GameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);

        gameGraphGenerator.generate();
        gameGraphGenerator.print();
    }

    /**
     * Creates a test case from
     * {@link ActionFactory#createMenuAction()} not active as it causes loops
     */
    private static GameGraphGenerator initializeGraphActions(StateBinarization stateBinarization){


        BinarizedGameState menuState = new BinarizedGameState( BinarizedGameState.fromMenu(stateBinarization));
        GameGraphGenerator gameGraphGenerator = new GameGraphGenerator(menuState, stateBinarization);

        gameGraphGenerator.addUserAction( ActionFactory.createStartAction(Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(Available.Items.First.MAKE_UP, Available.Items.First.USED_MAKE_UP, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
//
        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(Available.Items.First.DOOR_HANDLE, Available.Items.First.SCREW, Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(Available.Items.First.SCREW, Available.Items.First.DOOR_HANDLE, Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Items.First.USED_COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createExitAction(Available.Items.First.USED_COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createDismantleAction(Available.Items.First.COMBINED_DOOR_HANDLE, EnumSet.of(Available.Items.First.SCREW, Available.Items.First.DOOR_HANDLE), Available.Levels.Main.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createZoomAction(Available.Items.First.TV, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM, Levels.First.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createBackAction(Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM, Available.Levels.First.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createMenuAction());

        return gameGraphGenerator;
    }



}
import EscapeGame.*;
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
//        binarization ();
        example ();
    }

    private static ArrayList<String> getRooms (){
        ArrayList<String> rooms = new ArrayList<>();
        rooms.add( "MENU");
        rooms.add( "FIRST_ROOM");
        rooms.add( "THE END");
        return rooms;
    }
    private static ArrayList<String> getActions (){
        ArrayList<String> actions = new ArrayList<>();
        for ( AvailableActions.Option option:  AvailableActions.Option.values()){
            actions.add( option.name());
        }
        return actions;
    }
    private static ArrayList<String> getSubRooms (){
        ArrayList<String> subRooms = new ArrayList<>();
        subRooms.add( "LIVING_ROOM");
        subRooms.add( "TV_ROOM");
        return subRooms;
    }
    private static ArrayList<String> getItems() {
        ArrayList<String> items = new ArrayList<>();
        for ( AvailableItems.FirstRoom item:  AvailableItems.FirstRoom.values()){
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

        System.out.println("Game State Creator");
        GameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);

        gameGraphGenerator.generate();
        gameGraphGenerator.print();
    }

    private static void binarization (){
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        GameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);

        GameState menuState = GameState.fromMenu();

        UserAction startAction = ActionFactory.createStartAction(AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM);
        GameState nextState = menuState.apply( startAction);
        UserAction pickAction = ActionFactory.createPickAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM);


        {
            System.out.println( "*******************");
            System.out.println( menuState.toString());
            long binary = stateBinarization.binarize(menuState.getCondition());
            System.out.println("Binary: " + binary);
            System.out.println( stateBinarization.debinarize( binary));
        }
        {
            System.out.println( "*******************");
            System.out.println( nextState.toString());
            long binary = stateBinarization.binarize(nextState.getCondition());
            System.out.println("Binary: " + binary);
            System.out.println( stateBinarization.debinarize( binary));

        }
        {
            nextState = nextState.apply( pickAction);
            System.out.println( "*******************");
            System.out.println( nextState.toString());
            long binary = stateBinarization.binarize(nextState.getCondition());
            System.out.println("Binary: " + binary);
            System.out.println( stateBinarization.debinarize( binary));

        }
    }



    /**
     * Creates a test case from
     * {@link ActionFactory#createMenuAction()} not active as it causes loops
     */
    private static GameGraphGenerator initializeGraphActions(StateBinarization stateBinarization){


        BinarizedGameState menuState = new BinarizedGameState( BinarizedGameState.fromMenu(stateBinarization));
        GameGraphGenerator gameGraphGenerator = new GameGraphGenerator(menuState, stateBinarization);

        gameGraphGenerator.addUserAction( ActionFactory.createStartAction(AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(AvailableItems.FirstRoom.MAKE_UP, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(AvailableItems.FirstRoom.MAKE_UP, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(AvailableItems.FirstRoom.MAKE_UP, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(AvailableItems.FirstRoom.MAKE_UP, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(AvailableItems.FirstRoom.MAKE_UP, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(AvailableItems.FirstRoom.MAKE_UP, AvailableItems.FirstRoom.USED_MAKE_UP, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(AvailableItems.FirstRoom.SCREW, AvailableRooms.FIRST_ROOM, AvailableRooms.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(AvailableItems.FirstRoom.SCREW, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(AvailableItems.FirstRoom.SCREW, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(AvailableItems.FirstRoom.SCREW, AvailableRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(AvailableItems.FirstRoom.SCREW, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(AvailableItems.FirstRoom.DOOR_HANDLE, AvailableItems.FirstRoom.SCREW, AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(AvailableItems.FirstRoom.SCREW, AvailableItems.FirstRoom.DOOR_HANDLE, AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, AvailableItems.FirstRoom.USED_COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createExitAction(AvailableItems.FirstRoom.USED_COMBINED_DOOR_HANDLE, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createDismantleAction(AvailableItems.FirstRoom.COMBINED_DOOR_HANDLE, EnumSet.of(AvailableItems.FirstRoom.SCREW, AvailableItems.FirstRoom.DOOR_HANDLE), AvailableRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createZoomAction(AvailableItems.FirstRoom.TV, AvailableRooms.FIRST_ROOM, AvailableRooms.LIVING_ROOM, AvailableRooms.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createBackAction(AvailableRooms.FIRST_ROOM, AvailableRooms.TV_ROOM, AvailableRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createMenuAction());

        return gameGraphGenerator;
    }



}
import java.util.ArrayList;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
class Main {

    /**
     * Main method calls {@link #initializeGraphActions()}
     * @param args Not used
     */
    public static void main(String[] args){
        binarization ();
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
        for ( EscapeGameAction.Option option:  EscapeGameAction.Option.values()){
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
        for ( GameItems.FirstRoom item:  GameItems.FirstRoom.values()){
            items.add( item.name());
        }
        return items;
    }

    private static void example (){
        System.out.println("Game State Creator");
        GameGraphGenerator gameGraphGenerator = initializeGraphActions();

        ArrayList<String> startSeq = new ArrayList<>();
        startSeq.add("START GAME");
        startSeq.add("PICK DOOR_HANDLE");
        startSeq.add("ZOOM TV");

        gameGraphGenerator.playSequence(startSeq);

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

        GameGraphGenerator gameGraphGenerator = initializeGraphActions();

        GameState menuState = GameState.fromMenu();

        UserAction startAction = ActionFactory.createStartAction(GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM);
        GameState nextState = menuState.apply( startAction);
        UserAction pickAction = ActionFactory.createPickAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM);


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
    private static GameGraphGenerator initializeGraphActions(){


        GameState menuState = GameState.fromMenu();
        GameGraphGenerator gameGraphGenerator = new GameGraphGenerator(menuState);

        gameGraphGenerator.addUserAction( ActionFactory.createStartAction(GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));

//        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));
//        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
//        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
//        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
//        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
//        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(GameItems.FirstRoom.SCREW, GameRooms.FIRST_ROOM, GameRooms.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(GameItems.FirstRoom.SCREW, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(GameItems.FirstRoom.SCREW, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(GameItems.FirstRoom.SCREW, GameRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(GameItems.FirstRoom.SCREW, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectCombineAction(GameItems.FirstRoom.DOOR_HANDLE, GameRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(GameItems.FirstRoom.DOOR_HANDLE, GameItems.FirstRoom.SCREW, GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createCombineAction(GameItems.FirstRoom.SCREW, GameItems.FirstRoom.DOOR_HANDLE, GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, GameItems.FirstRoom.USED_COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createExitAction(GameItems.FirstRoom.USED_COMBINED_DOOR_HANDLE, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));

//        gameGraphGenerator.addUserAction( ActionFactory.createDismantleAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, EnumSet.of(GameItems.FirstRoom.SCREW, GameItems.FirstRoom.DOOR_HANDLE), GameRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createZoomAction(GameItems.FirstRoom.TV, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM, GameRooms.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createBackAction(GameRooms.FIRST_ROOM, GameRooms.TV_ROOM, GameRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createMenuAction());

        return gameGraphGenerator;
    }



}
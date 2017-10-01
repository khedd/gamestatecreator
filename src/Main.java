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
     * Main method calls {@link #createTests()}
     * @param args Not used
     */
    public static void main(String[] args){
        System.out.println("Game State Creator");
        createTests();

    }


    /**
     * Creates a test case from
     * {@link ActionFactory#createMenuAction()} not active as it causes loops
     */
    private static void createTests(){

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

        gameGraphGenerator.generate();
        gameGraphGenerator.print();
    }



}
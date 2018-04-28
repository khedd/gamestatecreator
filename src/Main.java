import graph.GameGraph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

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
        weightedExample ();
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
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        System.out.println("Game State Creator");
        BinarizedGameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);

        gameGraphGenerator.generate();

        gameGraphGenerator.printStatistics();

        ArrayList<ArrayList<String>> sequences = loadSequences( "");
        gameGraphGenerator.playSequences ( sequences);
        gameGraphGenerator.printCoverage ();

    }

    /**
     * use this to get the scoring of a persona finisher whose goal is to finish the
     * level as soon as possible
     * @return Scoring table filled with weights that show the priorities of this
     * persona
     */
    private static HashMap<String, Double> getScoringTableFinisher (){
        HashMap<String, Double> scoreTable = new HashMap<>();
        scoreTable.put("PICK DOOR_HANDLE", 3.0);
        scoreTable.put("PICK MAKE_UP", -3.0);
        scoreTable.put("PICK SCREW", 8.0);
        scoreTable.put("SELECT DOOR_HANDLE", -5.0);
        scoreTable.put("COMBINE SCREW DOOR_HANDLE => COMBINED_DOOR_HANDLE", 4.0);
        scoreTable.put("SELECT COMBINED_DOOR_HANDLE", 3.0);
        scoreTable.put("EXIT USED_COMBINED_DOOR_HANDLE", 8.0);


        return scoreTable;
    }

    /**
     * converts the given nodes to a sequence
     * @param nodes consists of {@link graph.GameGraph.GameGraphNode}
     * @return sequence with only actions
     */
    private static ArrayList<String> toSequence (ArrayList<GameGraph.GameGraphNode<Long>> nodes){
        ArrayList<String> sequence = new ArrayList<>();
        //remove the first and last elements as these are are decoys for fake
        //begin and end state in the current implementation
        for (int i = 1; i < nodes.size() - 1; i++) {
            sequence.add( nodes.get(i).action);
        }
        return sequence;
    }

    /**
     * this example uses weighted scoring to get the paths generated
     * 28.04.2018 as a part of testing how the weighted MCTS will go
     */
    private static void weightedExample (){
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        System.out.println("Game State Creator");
        BinarizedGameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);
        gameGraphGenerator.generate();

        double coveragePercent = 0;
        double defaultScore = -1;
        HashMap<String, Double> scoreTable = getScoringTableFinisher();

//        scoreTable = null;
        for (int i = 0; i < 20; i++) {

            ArrayList<GameGraph.GameGraphNode<Long>> visitedNodes;
            visitedNodes = gameGraphGenerator.testWMCTS( 500, scoreTable, defaultScore);
            ArrayList<String> sequence = toSequence ( visitedNodes);
            gameGraphGenerator.playSequence ( sequence);
            coveragePercent = gameGraphGenerator.getEdgeCoveragePercent ();
            System.out.println( "Coverage percent at iteration " + i + " :" + coveragePercent);
        }
    }


    // TODO: 10.12.2017 read from file
    private static ArrayList<ArrayList<String>> loadSequences ( String filename){
        ArrayList<ArrayList<String>> sequences = new ArrayList<>();
        ArrayList<String> seq1 = new ArrayList<>();
        seq1.add("START GAME");
        seq1.add("PICK DOOR_HANDLE");
        seq1.add("ZOOM TV");
        ArrayList<String> seq2 = new ArrayList<>();
        seq2.add( "START GAME");
        seq2.add( "RETURN MENU");
        seq2.add( "START GAME");
        seq2.add( "PICK DOOR_HANDLE");
        seq2.add( "PICK MAKE_UP");
        seq2.add( "ZOOM TV");
        seq2.add( "PICK SCREW");
        seq2.add( "BACK");
        seq2.add( "SELECT DOOR_HANDLE");
        seq2.add( "SELECT_COMBINE DOOR_HANDLE");
        seq2.add( "COMBINE DOOR_HANDLE SCREW => COMBINED_DOOR_HANDLE");
        seq2.add( "SELECT COMBINED_DOOR_HANDLE");
        seq2.add( "DISMANTLE COMBINED_DOOR_HANDLE => DOOR_HANDLE,SCREW");
        seq2.add( "SELECT DOOR_HANDLE");
        seq2.add( "SELECT_COMBINE DOOR_HANDLE");
        seq2.add( "COMBINE DOOR_HANDLE SCREW => COMBINED_DOOR_HANDLE");
        seq2.add( "SELECT COMBINED_DOOR_HANDLE");
        seq2.add( "SELECT_USE COMBINED_DOOR_HANDLE");
        seq2.add( "USE COMBINED_DOOR_HANDLE => USED_COMBINED_DOOR_HANDLE");
        seq2.add( "EXIT USED_COMBINED_DOOR_HANDLE");
        sequences.add( seq1);
        sequences.add( seq2);

        ArrayList<String> seq3 = new ArrayList<>();
        seq3.add( "START GAME");
        seq3.add( "RETURN MENU");
        seq3.add( "START GAME");
        seq3.add( "PICK DOOR_HANDLE");
        seq3.add( "PICK MAKE_UP");
        seq3.add( "SELECT MAKE_UP");
        seq3.add( "SELECT_EXIT MAKE_UP");

        seq3.add( "ZOOM TV");
        seq3.add( "PICK SCREW");
        seq3.add( "BACK");
        seq3.add( "SELECT DOOR_HANDLE");
        seq3.add( "SELECT_COMBINE DOOR_HANDLE");
        seq3.add( "COMBINE DOOR_HANDLE SCREW => COMBINED_DOOR_HANDLE");
        seq3.add( "SELECT COMBINED_DOOR_HANDLE");
        seq3.add( "DISMANTLE COMBINED_DOOR_HANDLE => DOOR_HANDLE,SCREW");
        seq3.add( "SELECT DOOR_HANDLE");
        seq3.add( "SELECT_COMBINE DOOR_HANDLE");
        seq3.add( "COMBINE DOOR_HANDLE SCREW => COMBINED_DOOR_HANDLE");
        seq3.add( "SELECT COMBINED_DOOR_HANDLE");
        seq3.add( "SELECT_USE COMBINED_DOOR_HANDLE");
        seq3.add( "USE COMBINED_DOOR_HANDLE => USED_COMBINED_DOOR_HANDLE");
        seq3.add( "EXIT USED_COMBINED_DOOR_HANDLE");
        sequences.add( seq3);

        return sequences;
    }

    private static void binarization (){
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        BinarizedGameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);

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
    private static BinarizedGameGraphGenerator initializeGraphActions(StateBinarization stateBinarization){


        BinarizedGameState menuState = BinarizedGameState.fromMenu(stateBinarization);
        BinarizedGameGraphGenerator gameGraphGenerator = new BinarizedGameGraphGenerator(menuState, stateBinarization);

        gameGraphGenerator.addUserAction( ActionFactory.createStartAction(GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createPickAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createDeselectAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectExitAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createSelectUseAction(GameItems.FirstRoom.MAKE_UP, GameRooms.FIRST_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createUseAction(GameItems.FirstRoom.MAKE_UP, GameItems.FirstRoom.USED_MAKE_UP, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM));

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

        gameGraphGenerator.addUserAction( ActionFactory.createDismantleAction(GameItems.FirstRoom.COMBINED_DOOR_HANDLE, EnumSet.of(GameItems.FirstRoom.SCREW, GameItems.FirstRoom.DOOR_HANDLE), GameRooms.FIRST_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createZoomAction(GameItems.FirstRoom.TV, GameRooms.FIRST_ROOM, GameRooms.LIVING_ROOM, GameRooms.TV_ROOM));
        gameGraphGenerator.addUserAction( ActionFactory.createBackAction(GameRooms.FIRST_ROOM, GameRooms.TV_ROOM, GameRooms.LIVING_ROOM));

        gameGraphGenerator.addUserAction( ActionFactory.createMenuAction());

        return gameGraphGenerator;
    }



}
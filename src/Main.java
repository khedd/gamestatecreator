import graph.GameGraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
class Main {
    private final static String OUT_PATH = "/home/khedd/Documents/thesis/2018/weekly/w19/results/";
    /**
     * Main method calls {@link #initializeGraphActions(StateBinarization)}
     * @param args Not used
     */
    public static void main(String[] args){
//        binarization ();

        int[] defScore = new int[]{-1,0,1};
        boolean[] useMem = new boolean[]{false, true};
        for (Persona persona: Persona.values()){
            for (int score: defScore){
                for (boolean mem: useMem){
                    weightedExample( createSettings( persona, score, mem));
                }
            }
        }

//        weightedExample ( getSettings( args));
    }

    @SuppressWarnings("Duplicates")
    private static Settings createSettings(Persona persona, int defScore, boolean useMem) {
        Settings settings = new Settings();
        settings.persona = persona;
        settings.defaultScore = defScore;
        settings.iterationCount = 10000;
        settings.rolloutCount = 5;
        settings.maxLen = 500;
        settings.useSaturation = true;
        settings.useMemory = useMem;
        settings.path = OUT_PATH;
        switch ( settings.persona){
            case Finisher:
                settings.scoreTable = getScoringTableFinisher();
                break;
            case Completionist:
                settings.scoreTable = getScoringTableCompletionist();
                break;
            case Default:
                settings.scoreTable = null;
                break;
            default:
                settings.scoreTable = null;
        }
        return settings;
    }

    private static Settings getSettings (String[] args){
        Settings settings = new Settings();
        if ( args.length == 0) {
            settings.persona = Persona.Default;
            settings.defaultScore = 1;
            settings.iterationCount = 10000;
            settings.rolloutCount = 5;
            settings.maxLen = 500;
            settings.useSaturation = false;
            settings.useMemory = true;
            settings.path = OUT_PATH;
            switch ( settings.persona){
                case Finisher:
                    settings.scoreTable = getScoringTableFinisher();
                    break;
                case Completionist:
                    settings.scoreTable = getScoringTableCompletionist();
                    break;
                case Default:
                    settings.scoreTable = null;
                    break;
                default:
                    settings.scoreTable = null;
            }
        }
        return settings;
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
     * use this to get the scoring of a persona finisher whose goal is to finish the
     * level as soon as possible
     * @return Scoring table filled with weights that show the priorities of this
     * persona
     */
    private static HashMap<String, Double> getScoringTableCompletionist (){
        HashMap<String, Double> scoreTable = new HashMap<>();
        scoreTable.put("PICK DOOR_HANDLE", 3.0);
        scoreTable.put("PICK MAKE_UP", 5.0);
        scoreTable.put("PICK SCREW", 8.0);
        scoreTable.put("USE MAKE_UP => USED_MAKE_UP", 5.0);
        scoreTable.put("COMBINE SCREW DOOR_HANDLE => COMBINED_DOOR_HANDLE", 4.0);
        scoreTable.put("DISMANTLE COMBINED_DOOR_HANDLE => DOOR_HANDLE,SCREW", 2.0);
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
     * @param settings
     */
    private static void weightedExample(Settings settings){
        StateBinarization stateBinarization = new StateBinarization();
        stateBinarization.addActions( getActions());
        stateBinarization.addItems( getItems());
        stateBinarization.addRooms( getRooms());
        stateBinarization.addSubRooms( getSubRooms());
        stateBinarization.generate ();

        BinarizedGameGraphGenerator gameGraphGenerator = initializeGraphActions( stateBinarization);
        gameGraphGenerator.generate();
        gameGraphGenerator.createMemory();

        BufferedWriter covFileWriter = null;
        BufferedWriter actFileWriter = null;
        try {
            covFileWriter = new BufferedWriter(new FileWriter(settings.createFilename()));
            actFileWriter = new BufferedWriter(new FileWriter(settings.createFilenameActions(false)));
            initCovFileHeader( covFileWriter);
            initActFileHeader ( actFileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Testing Coverage for persona: " + settings.persona.name() + " memory: " +
        settings.useMemory + " curiosity level: " + settings.defaultScore);
        coverage(gameGraphGenerator, covFileWriter, settings);

        if (covFileWriter != null){
            try {
                covFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (actFileWriter != null){
            try {
                HashMap<String, Long> unvisitedHistogram = gameGraphGenerator.getUnvisitedHistogram();
                ArrayList<String> actionHistograms = new ArrayList<>();
                unvisitedHistogram.entrySet().stream()
                        .sorted(Comparator.comparing(Map.Entry::getValue))
                        .forEach(k -> actionHistograms.add(k.getKey() + "," + k.getValue()));
                for (String element: actionHistograms){
                    actFileWriter.write(element + "\n");
//                    System.out.println(element.getKey() + ": " + element.getValue());
                }
                actFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initActFileHeader(BufferedWriter writer) {
        String line = "action,count\n";
        try {
            writer.write( line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void coverage (BinarizedGameGraphGenerator gameGraphGenerator, BufferedWriter writer,
                                  Settings settings){
        if ( settings.useMemory)
            gameGraphGenerator.activateMemory();

        ArrayList<Pair> coveragePercents = new ArrayList<>();
        Pair curPair = null;
        double coveragePercent;

        //do some runs to increase edge coverage
        for (int i = 0; i < settings.iterationCount; i++) {
            ArrayList<GameGraph.GameGraphNode<Long>> visitedNodes;
            visitedNodes = gameGraphGenerator.testWMCTS( settings.maxLen, settings.scoreTable, settings.defaultScore);
            ArrayList<String> sequence = toSequence ( visitedNodes);
            gameGraphGenerator.playSequence ( sequence);
            coveragePercent = gameGraphGenerator.getEdgeCoveragePercent ();
            if ( settings.useSaturation) {
                if (i == 0) {
                    curPair = new Pair(null, coveragePercent);
                    coveragePercents.add(curPair);
                } else {
                    if (curPair == null || !curPair.count(coveragePercent)) {
                        curPair = new Pair(curPair, coveragePercent);
                        coveragePercents.add(curPair);
                    }
                }
            }
            if ( (i + 1) % 1000 == 0)
                System.out.println( "Coverage percent at iteration " + (i + 1) + " :" + coveragePercent);
            recordToFile (writer, i, coveragePercent);
        }

        //for debugging
        if ( settings.useSaturation) {
            //print all the pairs
            for (Pair p : coveragePercents) {
                System.out.println(p.toString());
            }
        }


    }

    private static void initCovFileHeader(BufferedWriter writer){
        String line = "iteration,coveragePercent\n";
        try {
            writer.write( line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recordToFile(BufferedWriter writer, int iteration, double coveragePercent) {
        if ( writer != null){
            String line = "" + (iteration + 1) + "," + coveragePercent + "\n";
            try {
                writer.write( line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Settings{
        int iterationCount;
        int rolloutCount;
        int maxLen;
        HashMap<String, Double> scoreTable;
        int defaultScore;
        boolean useMemory;
        boolean useSaturation;
        Persona persona;
        String path;

        String createFilename (){
            int mem = useMemory ? 1 : 0;
            return path + "persona_" + persona.name() + "_memory_" + mem + "_def_" +
                    defaultScore + "_iter_" + iterationCount + "_len_" + maxLen +
                    ".csv";
        }
        String createFilenameActions(boolean isTaken){
            int mem = useMemory ? 1 : 0;
            int isT = isTaken ? 1 : 0;
            return path + "actions_"+ isT + "_persona_" + persona.name() + "_memory_" + mem + "_def_" +
                    defaultScore + "_iter_" + iterationCount + "_len_" + maxLen +
                    ".csv";
        }
    }

    private enum Persona{
        Finisher, Completionist, Default
    }

    /**
     * class to be utilized during saturation
     */
    private static class Pair{
        double increase;
        double percent;
        int count;
        Pair(Pair prev, double coveragePercent){
            percent = coveragePercent;
            count = 1;
            if ( prev == null){
                increase = coveragePercent;
            }else{
                increase = coveragePercent - prev.percent;
            }
        }
        boolean count ( double coveragePercent){
            if ( percent == coveragePercent){
                count++;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {

            return "Increase: " + increase + " count: " + count + " total percent: " + percent;
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
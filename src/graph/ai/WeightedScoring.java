package graph.ai;

import graph.GameGraph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a weighted scoring with decay to evaluate the rollout
 * @param <T> type of the {@link graph.GameGraph.GameGraphNode}
 */
public class WeightedScoring<T> extends ScoringPolicy<T> {

    /**
     * holds the scoring pairs that will be used during the evaluate section
     * if the scoring is not found
     */
    private final HashMap<String, Double> mScoreTable;
    /**
     * this score will be used if the action cannot be found on the HashMap
     */
    private final double mDefaultScore;

    /**
     * Initializes the scoring to be utilized
     * @param scoreTable HashMap with "Action", "Reward" pairs
     * @param defaultScore the score to be utilized if the action cannot be found
     */
    public WeightedScoring ( HashMap<String, Double> scoreTable, double defaultScore){
        mScoreTable = scoreTable;
        mDefaultScore = defaultScore;
    }

    /**
     * evaluate the path taken with the scoring mechanism, uses decay value of 0.1
     * to lower the rewards obtained from visiting depths of the tree
     * @param nodes nodes obtained from rollout
     * @return total score obtained from visiting the nodes
     */
    @Override
    public double evaluate(ArrayList<GameGraph.GameGraphNode<T>> nodes) {
        double totalScore = 0;
        double decay = 0.1;
        for (int i = 0; i < nodes.size(); i++) {
            GameGraph.GameGraphNode<T> node = nodes.get(i);
            Double score = mScoreTable.get( node.action);
            if ( score != null){
                totalScore += Math.pow( 1.0 - decay, i) * score;
            }else{
                totalScore += Math.pow( 1.0 - decay, i) * mDefaultScore;
            }
        }
        return totalScore;
    }
}

package graph.ai;


public class UCB1<T> extends SelectionCriteria<T> {
    /**
     * bias term to choose between exploration and exploitation
     */
    final private double mBias;

    /**
     * construct an UCB1 object that evaluates the nodes
     * @param bias bias term in the UCB1 formula
     */
    public UCB1 (double bias){
        mBias = bias;
    }

    /**
     * Calculates the UCB1 score of the tree
     * @param totalVisits Total Number of visits in total tree
     * @param stateInformation Node state
     * @return UCB1 score
     */
    @Override
    public double calculate(int totalVisits, StateInformation stateInformation) {
        if ( stateInformation.visit == 0)
            return Double.MAX_VALUE;
        return stateInformation.score + Math.sqrt( mBias * Math.log((double)totalVisits) / (double)stateInformation.visit);
    }
}

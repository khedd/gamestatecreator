package graph.ai;

public abstract class SelectionCriteria<T>{
    /**
     * calculates a nodes value
     * @param totalVisits Total Number of visits in total tree
     * @param stateInformation Node state
     * @return selection score
     */
    public abstract double calculate(int totalVisits, StateInformation stateInformation);
}


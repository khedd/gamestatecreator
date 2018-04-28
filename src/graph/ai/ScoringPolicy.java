package graph.ai;

import graph.GameGraph;

import java.util.ArrayList;

public abstract class ScoringPolicy<T> {
    public abstract double evaluate(ArrayList<GameGraph.GameGraphNode<T>> nodes);
}
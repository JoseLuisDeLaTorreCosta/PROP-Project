package main.Domain.Hill_Climbing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class HillClimbing {
    State state;
    SuccessorFunction sf;
    HeuristicFunction hf;
    Node LastNode;

    //Classe privada que representa un node a partir d'un estat
    private class Node {
        State state;
        Node parent;
        int depth;
        Double stepCost;
        Double pathCost;
        
        //Creadora d'un node arrel a partir d'un estat
        public Node (State state) {
            this.state = state;
            this.depth = 0;
            this.stepCost = 0.00;
            this.pathCost = 0.00;
        }

        //Creadora d'un node a partir d'un estat amb pare passat per parametre
        public Node (Node parent, State state) {
            this(state);
            this.parent = parent;
            this.depth = parent.getDepth() + 1;
        }

        //Retorna l'estat que representa el node
        public State getState() {
            return state;
        }

        //Retorna a quina profunditat esta el node dins de l'arbre de solucions
        public int getDepth() {
            return depth;
        }

        //Assigna el cost de fer un pas cap a aquest node des de l'arrel fins al node actual
        public void setStepCost(double newStepCost) {
            this.stepCost = newStepCost;
        }

        //Assigna el cost de fer un pas cap a aquest node des de l'arrel fins al node actual a partir del pare
        public void addToPathCost(double stepCost) {
            this.stepCost = this.parent.pathCost + stepCost;
        }
    }


    //Creadora de la clase HillClimbing
    public HillClimbing(State state, SuccessorFunction sf) {
        this.state = state;
        this.sf = sf;
        hf = new HeuristicFunction();
    }

    //Funcio que s'encarrega de fer la cerca heuristica
    public void search() throws Exception {
        Node current = new Node(state);
        Node neighbor = null;
        while (true) {
            List children = expandNode(current);
            neighbor = getHighestValuedNodeFrom(children);
            if (neighbor == null || -1*hf.getHeuristicValue(neighbor.getState()) <= -1*hf.getHeuristicValue(current.getState())) {
                this.state = current.getState();
                this.LastNode = current;
                break;
            }
            current = neighbor;
        }
    }

    //Funcio que retorna l'estat despres de la cerca
    public State getState() {
        return state;
    }

    //Funció que retorna el node amb mes valor dins de la llista de fills
    private Node getHighestValuedNodeFrom(List<Node> children) {
        double highestValue = Double.NEGATIVE_INFINITY;
        Node nodeWithHighestValue = null;
        for (int i = 0; i < children.size(); i++) {
          Node child = children.get(i);
          double value = -1*hf.getHeuristicValue(child.getState());
          if (value > highestValue) {
            highestValue = value;
            nodeWithHighestValue = child;
          }
        }
        return nodeWithHighestValue;
    }

    //Funcio que s'encarrega d'expandir un node, de forma que retorna una llista dels seus successors
    public List expandNode(Node node) {
        double heuristic = hf.getHeuristicValue(node.getState());
        List<Node> nodes = new ArrayList();
        List<State> successors = sf.getSuccessors(node.getState());
        for (int i = 0; i < successors.size(); i++) {
            State successor = successors.get(i);
            Node aNode = new Node(node, successor);
            Double stepCost = hf.getHeuristicValue(node.getState());
            aNode.setStepCost(stepCost);
            aNode.addToPathCost(stepCost);
            nodes.add(aNode);
        }
        return nodes;
    }

     

}

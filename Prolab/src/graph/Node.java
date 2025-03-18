package graph;

import model.stop.Stop;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Stop stop;
    private List<Node> neighbors;
    private double costToReach;
    private Node previousNode;
    private boolean visited;

    public Node(Stop stop) {
        this.stop = stop;
        this.neighbors = new ArrayList<>();
        this.costToReach = Double.MAX_VALUE;
        this.visited = false;
    }

    public Stop getStop() {
        return stop;
    }

    public void addNeighbor(Node neighbor) {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public double getCostToReach() {
        return costToReach;
    }

    public void setCostToReach(double cost) {
        this.costToReach = cost;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double distanceTo(Node other) {
        if (other == null || this.stop == null || other.getStop() == null) {
            return Double.MAX_VALUE;
        }

        double lat1 = this.stop.getLat();
        double lon1 = this.stop.getLon();
        double lat2 = other.getStop().getLat();
        double lon2 = other.getStop().getLon();

        return calculateDistance(lat1, lon1, lat2, lon2);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
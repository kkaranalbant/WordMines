package graph;

import model.cost.Cost;

import java.util.List;

public class Route {
    private List<Node> path;
    private Cost totalCost;
    private int transfers;

    public Route(List<Node> path, Cost totalCost, int transfers) {
        this.path = path;
        this.totalCost = totalCost;
        this.transfers = transfers;
    }

    public List<Node> getPath() {
        return path;
    }

    public Cost getTotalCost() {
        return totalCost;
    }

    public int getTransfers() {
        return transfers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Route Details:\n");
        sb.append(String.format("Total Price: %.2f TL\n", totalCost.getPrice()));
        sb.append(String.format("Total Time: %.2f minutes\n", totalCost.getTime()));
        sb.append(String.format("Total Distance: %.2f km\n", totalCost.getDistance()));
        sb.append(String.format("Number of Transfers: %d\n", transfers));
        sb.append("Path: ");

        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).getStop().getId());
            if (i < path.size() - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }
}
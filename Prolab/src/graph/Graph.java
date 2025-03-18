package graph;

import model.cost.Cost;
import model.stop.Stop;
import model.stop.StopToStop;
import model.stop.Transfer;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes;
    private Map<String, Map<String, Cost>> edgeCosts;

    public Graph() {
        this.nodes = new HashMap<>();
        this.edgeCosts = new HashMap<>();
    }

    public void addNode(Stop stop) {
        nodes.putIfAbsent(stop.getId(), new Node(stop));
        edgeCosts.putIfAbsent(stop.getId(), new HashMap<>());

        if (stop.getStopToStops() != null) {
            for (StopToStop stopToStop : stop.getStopToStops()) {
                Cost cost = new Cost(
                        stopToStop.getPrice().doubleValue(),
                        stopToStop.getTime().doubleValue(),
                        stopToStop.getDistance().doubleValue()
                );
                edgeCosts.get(stop.getId()).put(stopToStop.getStopId(), cost);
            }
        }

        Transfer transfer = stop.getTransfer();
        if (transfer != null) {
            Cost transferCost = new Cost(
                    transfer.getTransferPrice().doubleValue(),
                    transfer.getTransferTime().doubleValue(),
                    0.0D
            );
            edgeCosts.get(stop.getId()).put(transfer.getTransferStopId(), transferCost);
        }
    }


    public void buildConnections() {
        for (Node node : nodes.values()) {
            Stop stop = node.getStop();
            if (stop.getStopToStops() != null) {
                for (StopToStop stopToStop : stop.getStopToStops()) {
                    Node targetNode = nodes.get(stopToStop.getStopId());
                    if (targetNode != null) {
                        node.addNeighbor(targetNode);
                    }
                }
            }

            Transfer transfer = stop.getTransfer();
            if (transfer != null) {
                Node transferNode = nodes.get(transfer.getTransferStopId());
                if (transferNode != null) {
                    node.addNeighbor(transferNode);
                }
            }
        }
    }


    public Cost getEdgeCost(String fromId, String toId) {
        Map<String, Cost> costs = edgeCosts.get(fromId);
        return costs != null ? costs.get(toId) : null;
    }

    public Node getNode(String stopId) {
        return nodes.get(stopId);
    }

    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    public void info() {
        System.out.println("\nGraph Information:");
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            System.out.println("\nNode ID: " + entry.getKey());
            System.out.println("Type: " + entry.getValue().getStop().getType());
            System.out.println("Neighbors:");

            for (Node neighbor : entry.getValue().getNeighbors()) {
                Cost cost = getEdgeCost(entry.getKey(), neighbor.getStop().getId());
                if (cost != null) {
                    System.out.printf("  -> %s (Price: %.2f, Time: %.2f min, Distance: %.2f km)\n",
                            neighbor.getStop().getId(),
                            cost.getPrice(),
                            cost.getTime(),
                            cost.getDistance()
                    );
                }
            }
        }
    }
}
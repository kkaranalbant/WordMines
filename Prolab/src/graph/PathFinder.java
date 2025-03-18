package graph;

import model.cost.Cost;
import model.passenger.Old;
import model.passenger.Passenger;
import model.passenger.Student;
import model.payment.Cash;
import model.payment.CityCard;
import model.payment.CreditCard;
import model.payment.Payment;
import model.vehicle.Taxi;
import ui.MainFrame;
import util.DistanceCalculator;

import java.util.*;

public class PathFinder {
    private final Graph graph;
    private final Taxi taxi;
    private final double thresholdDistance = 3.0;

    public PathFinder(Graph graph, Taxi taxi) {
        this.graph = graph;
        this.taxi = taxi;
    }

    public Map<String, List<Route>> findOptimalRoutes(Node end, Double userLat, Double userLon, Double targetLat, Double targetLon, Passenger passenger, List<Payment> payments) {
        Map<String, List<Route>> allRoutes = new HashMap<>();

        Node start = findNearestNode(userLat, userLon);

        StringBuilder taxiMsg = new StringBuilder();

        if (targetLat != null && targetLon != null) {
            end = findNearestNode(targetLat, targetLon);
        }

        if (start == null || end == null) {
            return allRoutes;
        }

        double startDistance = DistanceCalculator.calculateDistance(userLat, userLon, start.getStop().getLat(), start.getStop().getLon());
        double endDistance = targetLat != null && targetLon != null ?
                DistanceCalculator.calculateDistance(targetLat, targetLon, end.getStop().getLat(), end.getStop().getLon()) : 0;

        if (startDistance > thresholdDistance) {
            taxiMsg.append("Taxi required from user location to start stop.\n");
            double taxiFare = taxi.calculateFare(startDistance);
            if (canPay(taxiFare, payments, true)) {
                taxiMsg.append("Taxi fare: " + taxiFare).append("\n");
            } else {
                taxiMsg.append("Insufficient funds for taxi fare.\n");
                return allRoutes;
            }
        }

        if (endDistance > thresholdDistance) {
            taxiMsg.append("Taxi required from end stop to target location.\n");
            double taxiFare = taxi.calculateFare(endDistance);
            if (canPay(taxiFare, payments, true)) {
                taxiMsg.append("Taxi fare: " + taxiFare).append("\n");
            } else {
                taxiMsg.append("Insufficient funds for taxi fare.\n");
                return allRoutes;
            }
        }

        allRoutes.put("DISTANCE", findRoutes(start, end, CostType.DISTANCE, passenger, payments));
        allRoutes.put("PRICE", findRoutes(start, end, CostType.PRICE, passenger, payments));
        allRoutes.put("TIME", findRoutes(start, end, CostType.TIME, passenger, payments));
        MainFrame.taxiMsg = taxiMsg.toString();
        return allRoutes;
    }

    private boolean canPay(double amount, List<Payment> payments, boolean isTaxi) {
        double totalCash = 0;
        double totalCreditCard = 0;
        double totalCityCard = 0;

        for (Payment payment : payments) {
            if (payment instanceof Cash) {
                totalCash += payment.getAmount();
            } else if (payment instanceof CreditCard) {
                totalCreditCard += payment.getAmount();
            } else if (payment instanceof CityCard) {
                totalCityCard += payment.getAmount();
            }
        }

        if (isTaxi) {
            return totalCash + totalCreditCard >= amount;
        } else {
            return totalCreditCard + totalCityCard >= amount;
        }
    }

    private List<Route> findRoutes(Node start, Node end, CostType costType, Passenger passenger, List<Payment> payments) {
        PriorityQueue<PathNode> queue = new PriorityQueue<>();
        Map<String, Double> costs = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        Set<String> visited = new HashSet<>();

        costs.put(start.getStop().getId(), 0.0);
        queue.offer(new PathNode(start, 0.0));

        Node nearestReachableNode = null;
        double minDistanceToEnd = Double.MAX_VALUE;
        List<Node> nearestReachablePath = new ArrayList<>();

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();
            Node currentNode = current.node;
            String currentId = currentNode.getStop().getId();

            if (visited.contains(currentId)) {
                continue;
            }

            visited.add(currentId);

            double distanceToEnd = DistanceCalculator.calculateDistance(
                    currentNode.getStop().getLat(), currentNode.getStop().getLon(),
                    end.getStop().getLat(), end.getStop().getLon()
            );

            if (distanceToEnd < minDistanceToEnd) {
                minDistanceToEnd = distanceToEnd;
                nearestReachableNode = currentNode;

                List<Node> pathToNode = new ArrayList<>();
                String nodeId = currentId;
                while (nodeId != null) {
                    pathToNode.add(0, graph.getNode(nodeId));
                    nodeId = previousNodes.get(nodeId);
                }
                nearestReachablePath = pathToNode;
            }

            if (currentId.equals(end.getStop().getId())) {
                break;
            }

            for (Node neighbor : currentNode.getNeighbors()) {
                String neighborId = neighbor.getStop().getId();
                Cost edgeCost = graph.getEdgeCost(currentId, neighborId);

                if (edgeCost == null) {
                    continue;
                }

                double costValue = getCostValue(edgeCost, costType);
                double newCost = costs.get(currentId) + costValue;

                if (!costs.containsKey(neighborId) || newCost < costs.get(neighborId)) {
                    costs.put(neighborId, newCost);
                    previousNodes.put(neighborId, currentId);
                    queue.offer(new PathNode(neighbor, newCost));
                }
            }
        }

        if (!previousNodes.containsKey(end.getStop().getId())) {
            if (nearestReachableNode != null) {
                StringBuilder pathStr = new StringBuilder("Path to nearest reachable node: ");
                for (Node node : nearestReachablePath) {
                    pathStr.append(node.getStop().getId()).append(" -> ");
                }
                MainFrame.taxiMsg = pathStr + "Using taxi to destination";

                Cost pathCost = calculateTotalCost(nearestReachablePath, passenger, payments);
                Route route = new Route(nearestReachablePath, pathCost, calculateTransfers(nearestReachablePath));
                List<Route> routes = new ArrayList<>();
                routes.add(route);
                return routes;
            } else {
                return new ArrayList<>();
            }
        }

        return reconstructPath(start, end, previousNodes, passenger, payments);
    }


    private double getCostValue(Cost cost, CostType type) {
        return switch (type) {
            case DISTANCE -> cost.getDistance();
            case PRICE -> cost.getPrice();
            case TIME -> cost.getTime();
        };
    }

    private List<Route> reconstructPath(Node start, Node end, Map<String, String> previousNodes, Passenger passenger, List<Payment> payments) {
        List<Route> routes = new ArrayList<>();

        if (!previousNodes.containsKey(end.getStop().getId())) {
            return routes;
        }

        List<Node> path = new ArrayList<>();
        String currentId = end.getStop().getId();

        while (currentId != null) {
            path.add(0, graph.getNode(currentId));
            currentId = previousNodes.get(currentId);
        }

        Cost totalCost = calculateTotalCost(path, passenger, payments);
        int transfers = calculateTransfers(path);

        routes.add(new Route(path, totalCost, transfers));
        return routes;
    }

    private Cost calculateTotalCost(List<Node> path, Passenger passenger, List<Payment> payments) {
        double totalPrice = 0;
        double totalTime = 0;
        double totalDistance = 0;

        double cityCardBalance = 0;
        double creditCardBalance = 0;

        for (Payment payment : payments) {
            if (payment instanceof CityCard) {
                cityCardBalance = payment.getAmount();
            } else if (payment instanceof CreditCard) {
                creditCardBalance = payment.getAmount();
            }
        }

        double rawTotalPrice = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            Cost edgeCost = graph.getEdgeCost(current.getStop().getId(), next.getStop().getId());

            if (edgeCost != null) {
                rawTotalPrice += edgeCost.getPrice();
                totalTime += edgeCost.getTime();
                totalDistance += edgeCost.getDistance();
            }
        }

        if (passenger instanceof Student) {
            double discountedPrice = rawTotalPrice * 0.5;

            if (cityCardBalance >= discountedPrice) {
                totalPrice = discountedPrice;
                for (Payment payment : payments) {
                    if (payment instanceof CityCard) {
                        payment.setAmount(cityCardBalance - discountedPrice);
                        break;
                    }
                }
            } else if (creditCardBalance >= rawTotalPrice) {
                totalPrice = rawTotalPrice;
                for (Payment payment : payments) {
                    if (payment instanceof CreditCard) {
                        payment.setAmount(creditCardBalance - rawTotalPrice);
                        break;
                    }
                }
            } else {
                System.out.println("Insufficient funds in both CityCard and CreditCard");
            }
        } else if (passenger instanceof Old) {
            totalPrice = 0;
        } else {
            totalPrice = rawTotalPrice;
        }

        return new Cost(totalPrice, totalTime, totalDistance);
    }

    private int calculateTransfers(List<Node> path) {
        int transfers = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).getStop().getType() != path.get(i + 1).getStop().getType()) {
                transfers++;
            }
        }
        return transfers;
    }

    private static class PathNode implements Comparable<PathNode> {
        Node node;
        double cost;

        PathNode(Node node, double cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(PathNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    public Node findNearestNode(Double lat, Double lon) {
        Node nearestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : graph.getAllNodes()) {
            double distance = DistanceCalculator.calculateDistance(lat, lon, node.getStop().getLat(), node.getStop().getLon());
            if (distance < minDistance) {
                minDistance = distance;
                nearestNode = node;
            }
        }

        return nearestNode;
    }

    private enum CostType {
        DISTANCE, PRICE, TIME
    }

    public double getThresholdDistance() {
        return thresholdDistance;
    }
}
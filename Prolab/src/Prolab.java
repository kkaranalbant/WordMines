//import graph.Graph;
//import graph.Node;
//import graph.PathFinder;
//import graph.Route;
//import model.stop.Stop;
//import model.vehicle.Taxi;
//import util.Converter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//
//public class Prolab {
//    public static void main(String[] args) {
//        Graph graph = new Graph();
//
//        try {
//            List<Stop> stops = Converter.parseStops("/home/kaan/Downloads/veriseti.json");
//            Taxi taxi = Converter.parseTaxi("/home/kaan/Downloads/veriseti.json");
//
//            for (Stop stop : stops) {
//                graph.addNode(stop);
//            }
//
//            graph.buildConnections();
//
//
//            Node start = graph.getNode("bus_otogar");
//            Node end = graph.getNode("bus_41burda");
//
//            PathFinder pathFinder = new PathFinder(graph,taxi);
//
//            //Map<String, List<Route>> routes = pathFinder.findOptimalRoutes(start, end);
//
//            for (Map.Entry<String, List<Route>> entry : routes.entrySet()) {
//                System.out.println("\n" + entry.getKey() + " OPTIMAL ROUTE:");
//                for (Route route : entry.getValue()) {
//                    System.out.println("Total Cost: (price , time , distance) " + route.getTotalCost().getPrice() +","+ route.getTotalCost().getTime()+ "," +route.getTotalCost().getDistance());
//                    System.out.print("Path: ");
//                    for (Node node : route.getPath()) {
//                        System.out.print(node.getStop().getId() + " -> ");
//                    }
//                    System.out.println("END");
//                }
//            }
//
//            graph.info();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}
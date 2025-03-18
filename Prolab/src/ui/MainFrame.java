package ui;

import graph.Graph;
import graph.Node;
import graph.PathFinder;
import graph.Route;
import model.passenger.General;
import model.passenger.Old;
import model.passenger.Passenger;
import model.passenger.Student;
import model.payment.Cash;
import model.payment.CityCard;
import model.payment.CreditCard;
import model.payment.Payment;
import model.stop.Stop;
import model.vehicle.Taxi;
import util.Converter;
import util.DistanceCalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {


    private JTextField userLatField;
    private JTextField userLonField;
    private JTextField targetLat;
    private JTextField targetLon;
    private JComboBox<String> stopComboBox;
    private JComboBox<String> passengerTypeComboBox;
    private JTextField cashField;
    private JTextField creditCardField;
    private JTextField kentKartField;
    private JTextArea resultArea;
    private Graph graph;
    private Taxi taxi;
    public static String taxiMsg;


    static {
        taxiMsg = null;
    }

    public MainFrame() {
        setTitle("Route Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        userLatField = new JTextField(10);
        userLonField = new JTextField(10);
        stopComboBox = new JComboBox<>();
        passengerTypeComboBox = new JComboBox<>(new String[]{"General", "Student", "Old"});
        cashField = new JTextField(10);
        creditCardField = new JTextField(10);
        kentKartField = new JTextField(10);
        targetLat = new JTextField(10);
        targetLon = new JTextField(10);
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        userLatField.setToolTipText("Enter your latitude");
        userLonField.setToolTipText("Enter your longitude");
        stopComboBox.setToolTipText("Select a stop");
        targetLat.setToolTipText("Enter target latitude (optional)");
        targetLon.setToolTipText("Enter target longitude (optional)");
        passengerTypeComboBox.setToolTipText("Select passenger type");
        cashField.setToolTipText("Enter cash amount");
        creditCardField.setToolTipText("Enter credit card limit");
        kentKartField.setToolTipText("Enter KentKart balance");

        loadGraph();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("User Latitude:"));
        inputPanel.add(userLatField);
        inputPanel.add(new JLabel("User Longitude:"));
        inputPanel.add(userLonField);
        inputPanel.add(new JLabel("Select Stop:"));
        inputPanel.add(stopComboBox);
        inputPanel.add(new JLabel("Target Latitude:"));
        inputPanel.add(targetLat);
        inputPanel.add(new JLabel("Target Longitude:"));
        inputPanel.add(targetLon);
        inputPanel.add(new JLabel("Passenger Type:"));
        inputPanel.add(passengerTypeComboBox);
        inputPanel.add(new JLabel("Cash Amount:"));
        inputPanel.add(cashField);
        inputPanel.add(new JLabel("Credit Card Limit:"));
        inputPanel.add(creditCardField);
        inputPanel.add(new JLabel("KentKart Balance:"));
        inputPanel.add(kentKartField);

        JButton findRoutesButton = new JButton("Find Routes");
        findRoutesButton.setPreferredSize(new Dimension(200, 30));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(findRoutesButton);

        findRoutesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findOptimalRoutes();
            }
        });

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane, BorderLayout.SOUTH);

        cashField.setText("1000000");
        creditCardField.setText("1000000");
        kentKartField.setText("1000000");
    }

    private void loadGraph() {
        graph = new Graph();
        try {
            List<Stop> stops = Converter.parseStops("/home/kaan/Downloads/veriseti.json");
            taxi = Converter.parseTaxi("/home/kaan/Downloads/veriseti.json");

            for (Stop stop : stops) {
                graph.addNode(stop);
                stopComboBox.addItem(stop.getId());
            }

            graph.buildConnections();
            //graph.info();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void findOptimalRoutes() {
        double userLat = Double.parseDouble(userLatField.getText());
        double userLon = Double.parseDouble(userLonField.getText());
        String selectedStopId = (String) stopComboBox.getSelectedItem();
        Node end = selectedStopId != null ? graph.getNode(selectedStopId) : null;

        Double targetLatValue = targetLat.getText().isEmpty() ? null : Double.parseDouble(targetLat.getText());
        Double targetLonValue = targetLon.getText().isEmpty() ? null : Double.parseDouble(targetLon.getText());

        Passenger passenger;
        String passengerType = (String) passengerTypeComboBox.getSelectedItem();
        switch (passengerType) {
            case "Student":
                passenger = new Student();
                break;
            case "Old":
                passenger = new Old();
                break;
            default:
                passenger = new General();
                break;
        }

        List<Payment> payments = new ArrayList<>();
        payments.add(new Cash(Double.parseDouble(cashField.getText())));
        payments.add(new CreditCard(Double.parseDouble(creditCardField.getText())));
        payments.add(new CityCard(Double.parseDouble(kentKartField.getText())));

        PathFinder pathFinder = new PathFinder(graph, taxi);
        Map<String, List<Route>> routes = pathFinder.findOptimalRoutes(end, userLat, userLon, targetLatValue, targetLonValue, passenger, payments);

        resultArea.setText("");
        for (Map.Entry<String, List<Route>> entry : routes.entrySet()) {
            resultArea.append("\n" + entry.getKey() + " OPTIMAL ROUTE:\n");
            for (Route route : entry.getValue()) {
                resultArea.append("Total Cost: (price, time, distance) " + route.getTotalCost().getPrice() + ", " +
                        route.getTotalCost().getTime() + ", " + route.getTotalCost().getDistance() + "\n");
                resultArea.append("Path: ");
                for (Node node : route.getPath()) {
                    resultArea.append(node.getStop().getId() + " -> ");
                }
                resultArea.append("END\n");

                if (taxiMsg != null) {
                    Node lastNode = route.getPath().get(route.getPath().size() - 1);
                    double taxiDistance = DistanceCalculator.calculateDistance(
                            lastNode.getStop().getLat(), lastNode.getStop().getLon(),
                            end.getStop().getLat(), end.getStop().getLon()
                    );
                    double taxiFare = taxi.calculateFare(taxiDistance);

                    resultArea.append("\nTaxi Information:\n");
                    resultArea.append("- " + taxiMsg + "\n");
                    resultArea.append("- Distance to be covered by taxi: " + String.format("%.2f", taxiDistance) + " km\n");
                    resultArea.append("- Taxi fare: " + String.format("%.2f", taxiFare) + " TL\n");
                    resultArea.append("- Starting point: " + lastNode.getStop().getId() + "\n");
                    resultArea.append("- Destination: " + end.getStop().getId() + "\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
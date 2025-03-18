package util;

import model.stop.Stop;
import model.stop.StopToStop;
import model.stop.Transfer;
import model.stop.Type;
import model.vehicle.Taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter {


    public static Taxi parseTaxi(String path) {
        Taxi taxi = new Taxi();
        File file = new File(path);
        try (FileReader fileReader = new FileReader(file); BufferedReader br = new BufferedReader(fileReader)) {
            String line = null;
            boolean foundOpeningFee = false;
            boolean foundCostPerKm = false;
            while ((line = br.readLine()) != null) {
                if (foundCostPerKm && foundOpeningFee) break;
                String key = JsonUtil.getKey(line);
                String value = JsonUtil.getValue(line);
                if (key != null && key.equalsIgnoreCase("openingFee")) {
                    taxi.setOpeningFee(Float.parseFloat(value));
                    foundOpeningFee = true;
                } else if (key != null && key.equalsIgnoreCase("costPerKm")) {
                    taxi.setCostPerKm(Float.parseFloat(value));
                    foundCostPerKm = true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return taxi;
    }

    public static List<Stop> parseStops(String path) throws IOException {
        List<Stop> stops = new ArrayList<>();
        File file = new File(path);
        String line = null;
        boolean foundId = false;
        boolean foundName = false;
        boolean foundType = false;
        boolean foundLat = false;
        boolean foundLon = false;
        boolean foundIsLastStop = false;
        boolean foundNextStops = false;
        boolean foundTransfer = false;
        boolean foundStopsKey = false;
        Stop stop = new Stop();
        try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
            int rowCounter = 1;
            int rowToContinueForStopToStops = 0;
            int rowToContinueForTransfer = 0;
            while ((line = br.readLine()) != null) {
                if (foundNextStops && rowCounter < rowToContinueForStopToStops) {
                    rowCounter++;
                    continue;
                }
                if (foundTransfer && rowCounter < rowToContinueForTransfer) {
                    rowCounter++;
                    continue;
                }
                String key = JsonUtil.getKey(line);
                String value = JsonUtil.getValue(line);
                if (key != null && key.equalsIgnoreCase("duraklar")) {
                    foundStopsKey = true;
                }
                if (foundStopsKey) {
                    if (key != null && key.equalsIgnoreCase("id")) {
                        stop.setId(value);
                        foundId = true;
                    } else if (key != null && key.equalsIgnoreCase("name")) {
                        stop.setName(value);
                        foundName = true;
                    } else if (key != null && key.equalsIgnoreCase("type")) {
                        Type[] types = Type.values();
                        for (Type type : types) {
                            if (type.name().equalsIgnoreCase(value)) {
                                stop.setType(type);
                                break;
                            }
                        }
                        foundType = true;
                    } else if (key != null && key.equalsIgnoreCase("lat")) {
                        stop.setLat(Double.parseDouble(value));
                        foundLat = true;
                    } else if (key != null && key.equalsIgnoreCase("lon")) {
                        stop.setLon(Double.parseDouble(value));
                        foundLon = true;
                    } else if (key != null && key.equalsIgnoreCase("sonDurak")) {
                        stop.setLastStop(Boolean.parseBoolean(value));
                        foundIsLastStop = true;
                    } else if (key != null && key.equalsIgnoreCase("nextStops")) {
                        // Boş nextStops dizisi için kontrol eklendi
                        if (value != null && value.trim().equals("[]")) {
                            // Boş dizi durumunda
                            stop.setStopToStops(new ArrayList<>());
                            foundNextStops = true;
                        } else {
                            Map<Integer, List<StopToStop>> rowListMap = getStopToStops(rowCounter, path);
                            List<StopToStop> stopToStops = null;
                            for (Map.Entry<Integer, List<StopToStop>> entry : rowListMap.entrySet()) {
                                rowToContinueForStopToStops = entry.getKey();
                                stopToStops = entry.getValue();
                            }
                            stop.setStopToStops(stopToStops);
                            foundNextStops = true;
                        }
                    } else if (key != null && key.equalsIgnoreCase("transfer")) {
                        // null transfer değeri için kontrol eklendi
                        if (value != null && value.trim().equalsIgnoreCase("null")) {
                            // null transfer durumunda
                            stop.setTransfer(null);
                            foundTransfer = true;
                            // Transfer null olduğunda bir sonraki satıra geçmek için rowCounter artırımı
                            rowToContinueForTransfer = rowCounter + 1;
                        } else {
                            Map<Integer, Transfer> rowTransferMap = getTransfer(rowCounter, path);
                            for (Map.Entry<Integer, Transfer> entry : rowTransferMap.entrySet()) {
                                stop.setTransfer(entry.getValue());
                                rowToContinueForTransfer = entry.getKey();
                            }
                            foundTransfer = true;
                        }
                    }

                    // Tüm gerekli bilgiler bulunduğunda durak eklenir
                    if (foundId && foundLat && foundName && foundLon && foundIsLastStop && foundType && foundNextStops && foundTransfer) {
                        stops.add(stop);
                        stop = new Stop();
                        foundId = false;
                        foundLat = false;
                        foundName = false;
                        foundLon = false;
                        foundIsLastStop = false;
                        foundType = false;
                        foundNextStops = false;
                        foundTransfer = false;
                    }
                }
                rowCounter++;
            }

        }
        return stops;
    }

    private static Map<Integer, List<StopToStop>> getStopToStops(int rowCounter, String path) throws IOException {
        Map<Integer, List<StopToStop>> rowListMap = new HashMap<>();
        List<StopToStop> stopToStops = new ArrayList<>();
        boolean foundStopId = false;
        boolean foundDistance = false;
        boolean foundTime = false;
        boolean foundPrice = false;
        StopToStop stopToStop = new StopToStop();
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int counter = 1;
        String line = null;
        while ((line = br.readLine()) != null) {
            if (counter < rowCounter) {
                counter++;
                continue;
            }
            String key = JsonUtil.getKey(line);
            String value = JsonUtil.getValue(line);
            if (key == null && (value.equalsIgnoreCase("]") || value.equalsIgnoreCase("],"))) {
                break;
            }
            if (key != null && key.equalsIgnoreCase("stopId")) {
                stopToStop.setStopId(value);
                foundStopId = true;
            } else if (key != null && key.equalsIgnoreCase("mesafe")) {
                stopToStop.setDistance(Float.parseFloat(value));
                foundDistance = true;
            } else if (key != null && key.equalsIgnoreCase("sure")) {
                stopToStop.setTime(Float.parseFloat(value));
                foundTime = true;
            } else if (key != null && key.equalsIgnoreCase("ucret")) {
                stopToStop.setPrice(Float.parseFloat(value));
                foundPrice = true;
            }
            if (foundStopId && foundDistance && foundTime && foundPrice) {
                stopToStops.add(stopToStop);
                stopToStop = new StopToStop();
                foundStopId = false;
                foundTime = false;
                foundPrice = false;
                foundDistance = false;
            }
            counter++;
        }
        br.close();
        rowListMap.put(counter, stopToStops);
        return rowListMap;
    }

    private static Map<Integer, Transfer> getTransfer(int rowCounter, String path) throws IOException {
        boolean foundTransferStopId = false;
        boolean foundTransferTime = false;
        boolean foundTransferPrice = false;
        Transfer transfer = new Transfer();
        String line = null;
        int counter = 1;
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        while ((line = br.readLine()) != null) {
            if (counter < rowCounter) {
                counter++;
                continue;
            }
            String key = JsonUtil.getKey(line);
            String value = JsonUtil.getValue(line);
            if (key != null && key.equalsIgnoreCase("transferStopId")) {
                transfer.setTransferStopId(value);
                foundTransferStopId = true;
            } else if (key != null && key.equalsIgnoreCase("transferSure")) {
                transfer.setTransferTime(Float.parseFloat(value));
                foundTransferTime = true;
            } else if (key != null && key.equalsIgnoreCase("transferUcret")) {
                transfer.setTransferPrice(Float.parseFloat(value));
                foundTransferPrice = true;
            }
            if (foundTransferTime && foundTransferPrice && foundTransferStopId) {
                br.readLine();
                br.readLine();
                break;
            }
            counter++;
        }
        br.close();
        Map<Integer, Transfer> rowTransferMap = new HashMap<>();
        rowTransferMap.put(counter, transfer);
        return rowTransferMap;
    }


}
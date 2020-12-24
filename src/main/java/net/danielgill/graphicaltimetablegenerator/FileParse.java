package net.danielgill.graphicaltimetablegenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileParse {
    private static String[] stations;
    private static String[] services;
    
    public FileParse(File tbfile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tbfile));
            
            String line;
            while((line = br.readLine()) != null) {
                if(line.equals("STATIONS")) {
                    parseStations(tbfile);
                } else if(line.equals("SERVICES")) {
                    parseServices(tbfile);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void parseStations(File tbfile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tbfile));
            String line;
            while((line = br.readLine()) != null) {
                if(line.equals("STATIONS")) {
                    line = br.readLine();
                    String[] stationsTemp = new String[Integer.parseInt(line)];
                    int count = 0;
                    while(!(line = br.readLine()).equals(";")) {
                        stationsTemp[count] = line;
                        count++;
                        System.out.println(line);
                    }
                    stations = stationsTemp;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void parseServices(File tbfile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tbfile));
            String line;
            while((line = br.readLine()) != null) {
                if(line.equals("SERVICES")) {
                    line = br.readLine();
                    String[] servicesTemp = new String[Integer.parseInt(line)];
                    for(int i = 0; i < servicesTemp.length; i++) {
                        servicesTemp[i] = "";
                        while(!(line = br.readLine()).equals(";")) {
                            servicesTemp[i] += (line + " ");
                        }
                    }
                    services = servicesTemp;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String[] serviceParse(String serviceInfo) {
        String[] infoSplit = serviceInfo.split(" ");
        String serviceID = infoSplit[0];
        String currentStation = "";
        String outputString = serviceID + ",";
        int counter = 0;
        for(int i = 1; i < infoSplit.length; i++) {
            if(infoSplit[i].matches("^.*[da]{1}[0-9]{1}.*$")) {
                counter = 0;
                outputString += currentStation + "-" + infoSplit[i] + ",";
            } else {
                if(counter > 0) {
                    currentStation += " " + infoSplit[i];
                } else {
                    currentStation = infoSplit[i];
                    counter++;
                }
            }
        }
        return outputString.split(",");
    }
    
    public static String[] getStations() {
        return stations;
    }
    
    public static String[] getServices() {
        return services;
    }
}

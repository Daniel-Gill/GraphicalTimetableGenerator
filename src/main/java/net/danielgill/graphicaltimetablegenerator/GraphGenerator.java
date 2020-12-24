package net.danielgill.graphicaltimetablegenerator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.ArrayUtils;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GraphGenerator extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    public GraphGenerator(String title, File xmlfile) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel(xmlfile);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Timetable", "Time", "Station", dataset);

        chart.setBackgroundPaint(Color.WHITE);
        
        String[] stations = {"Leeds", "Woodlesford", "Castleford", "Normanton", "Wakefield Kirkgate", "Darton", "Barnsley", "Wombwell", "Elsecar", "Chapeltown", "Meadowhall Interchange", "Sheffield"};
        
        String[] labels = new String[221];
        final int gap = 20;
        int count = gap;
        for(int i = 0; i < labels.length; i++) {
            if(count == gap) {
                labels[i] = stations[i/gap];
                count = 1;
            } else {
                labels[i] = "";
                count++;
            }
        }
        labels[labels.length - 1] = stations[stations.length - 1];
        
        SymbolAxis rangeAxis = new SymbolAxis("", labels);
        rangeAxis.setTickUnit(new NumberTickUnit(20));
        rangeAxis.setRange(0,225);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRangeAxis(rangeAxis);
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(true);
            renderer.setDefaultShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

        return chart;

    }

    private static XYDataset createDataset(File xmlfile) {
        
        String[] stations = getStations(xmlfile);
        System.out.println(Arrays.toString(stations));
        NodeList servicesNodeList = getServices(xmlfile);
        ArrayList<TimeSeries> timeSeriesList = new ArrayList<>();
        
        for(int i = 0; i < servicesNodeList.getLength(); i++) {
            Node serviceNode = servicesNodeList.item(i);
            Element serviceElement = (Element) serviceNode;
            TimeSeries temp = new TimeSeries(serviceElement.getAttribute("id"));
            NodeList stopNodeList = serviceElement.getChildNodes();
            for(int j = 0; j < stopNodeList.getLength(); j++) {
                Node stopNode = stopNodeList.item(j);
                Element stopElement = (Element) stopNode;
                if(stopElement.getElementsByTagName("arrtime") != null) {
                    String time = stopElement.getElementsByTagName("arrtime").item(0).getTextContent();
                    String station = stopElement.getElementsByTagName("station").item(0).getTextContent();
                    int stationNumber = findIndexOfStringArray(stations, station) * 20;
                    String[] timeSplit = time.split(":");
                    temp.add(new Minute(Integer.parseInt(timeSplit[1]), Integer.parseInt(timeSplit[0]), 1, 1, 1900), stationNumber);
                }
                if(stopElement.getElementsByTagName("deptime") != null) {
                    String time = stopElement.getElementsByTagName("deptime").item(0).getTextContent();
                    String station = stopElement.getElementsByTagName("station").item(0).getTextContent();
                    int stationNumber = findIndexOfStringArray(stations, station) * 20;
                    String[] timeSplit = time.split(":");
                    temp.add(new Minute(Integer.parseInt(timeSplit[1]), Integer.parseInt(timeSplit[0]), 1, 1, 1900), stationNumber);
                }
            }
            timeSeriesList.add(temp);
        }
        
        TimeSeriesCollection stationDataset = new TimeSeriesCollection();
        for(int i = 0; i < timeSeriesList.size(); i++) {
            stationDataset.addSeries(timeSeriesList.get(i));
        }

        return stationDataset;

    }
    
    public static JPanel createDemoPanel(File xmlfile) {
        JFreeChart chart = createChart(createDataset(xmlfile));
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
    
    public static String[] getStations(File xmlfile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(xmlfile);
        } catch (SAXException | IOException ex) {
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        doc.getDocumentElement().normalize();

        NodeList stationsNodeList = doc.getChildNodes();

        ArrayList<String> stationArrayList = new ArrayList<>();

        for (int i = 0; i < stationsNodeList.getLength(); i++) {
            Node stationNode = stationsNodeList.item(i);
            if(stationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element stationElement = (Element) stationNode;
                stationArrayList.add(stationElement.getAttribute("name"));
            }
        }

        return stationArrayList.toArray(new String[stationArrayList.size()]);
    }
    
    public static NodeList getServices(File xmlfile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Document doc = dBuilder.parse(xmlfile);
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName("service");
        } catch (SAXException | IOException ex) {
            Logger.getLogger(GraphGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static int findIndexOfStringArray(String[] array, String item) {
        for(int i = 0; i < array.length; i++) {
            if(array[i].equals(item)) {
                return i;
            }
        }
    }
}

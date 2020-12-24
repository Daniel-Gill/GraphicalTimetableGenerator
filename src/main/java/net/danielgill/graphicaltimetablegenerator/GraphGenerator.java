package net.danielgill.graphicaltimetablegenerator;

import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.swing.JPanel;

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

public class GraphGenerator extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    public GraphGenerator(String title, File tbfile) {
        super(title);
        
        FileParse fp = new FileParse(tbfile);
        System.out.println(Arrays.toString(fp.getStations()));
        System.out.println(Arrays.toString(fp.getServices()));
        System.out.println(Arrays.toString(fp.serviceParse(fp.getServices()[0])));
        
        ChartPanel chartPanel = (ChartPanel) createDemoPanel(fp);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private static JFreeChart createChart(XYDataset dataset, FileParse fp) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Timetable", "Time", "Stations", dataset);

        chart.setBackgroundPaint(Color.WHITE);
        
        String[] stations = fp.getStations();
        
        final int gap = 20;
        int count = gap;
        String[] labels = new String[stations.length * gap - (gap - 1)];
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
        rangeAxis.setTickUnit(new NumberTickUnit(gap));
        rangeAxis.setRange(0,stations.length * gap + 2.5);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRangeAxis(rangeAxis);
        plot.setBackgroundPaint(Color.WHITE);
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

    private static XYDataset createDataset(FileParse fp) {
        String[] services = fp.getServices();
        String[] stations = fp.getStations();
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        for(int i = 0; i < services.length; i++) {
            String[] serviceInfo = fp.serviceParse(services[i]);
            TimeSeries tempTS = new TimeSeries(serviceInfo[0]);
            for(int j = 1; j < serviceInfo.length; j++) {
                String[] stationSplit = serviceInfo[j].split("-");
                int stationValue = findIndexOfStringArray(stations, stationSplit[0]) * 20;
                tempTS.add(new Minute(Integer.parseInt(stationSplit[1].substring(4, 6)), Integer.parseInt(stationSplit[1].substring(1, 3)), 1, 1, 1900), stationValue);
            }
            dataset.addSeries(tempTS);
        }
        
        return dataset;
    }
    
    public static JPanel createDemoPanel(FileParse fp) {
        JFreeChart chart = createChart(createDataset(fp), fp);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
    
    public static int findIndexOfStringArray(String[] array, String item) {
        for(int i = 0; i < array.length; i++) {
            if(array[i].equals(item)) {
                return i;
            }
        }
        return 0;
    }
}
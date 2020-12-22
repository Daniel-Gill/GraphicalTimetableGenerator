package net.danielgill.graphicaltimetablegenerator;

import java.awt.Color;
import java.text.SimpleDateFormat;

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
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class GraphGenerator extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    public GraphGenerator(String title) {
        super(title);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
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

    private static XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries("2L63");
        s1.add(new Minute(32, 11, 1, 1, 1900), 0);
        s1.add(new Minute(41, 11, 1, 1, 1900), 20);
        s1.add(new Minute(49, 11, 1, 1, 1900), 40);
        s1.add(new Minute(52, 11, 1, 1, 1900), 40);
        s1.add(new Minute(57, 11, 1, 1, 1900), 60);
        s1.add(new Minute(2, 12, 1, 1, 1900), 80);
        s1.add(new Minute(3, 12, 1, 1, 1900), 80);
        s1.add(new Minute(14, 12, 1, 1, 1900), 100);
        s1.add(new Minute(21, 12, 1, 1, 1900), 120);
        s1.add(new Minute(22, 12, 1, 1, 1900), 120);
        s1.add(new Minute(27, 12, 1, 1, 1900), 140);
        s1.add(new Minute(31, 12, 1, 1, 1900), 160);
        s1.add(new Minute(36, 12, 1, 1, 1900), 180);
        s1.add(new Minute(42, 12, 1, 1, 1900), 200);
        s1.add(new Minute(43, 12, 1, 1, 1900), 200);
        s1.add(new Minute(51, 12, 1, 1, 1900), 220);
        
        TimeSeries s2 = new TimeSeries("1Y17");
        s2.add(new Minute(9, 12, 1, 1, 1900), 0);
        s2.add(new Minute(25, 12, 1, 1, 1900), 80);
        s2.add(new Minute(26, 12, 1, 1, 1900), 80);
        s2.add(new Minute(41, 12, 1, 1, 1900), 120);
        s2.add(new Minute(42, 12, 1, 1, 1900), 120);
        s2.add(new Minute(56, 12, 1, 1, 1900), 200);
        s2.add(new Minute(57, 12, 1, 1, 1900), 200);
        s2.add(new Minute(04, 13, 1, 1, 1900), 220);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;

    }
    
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
}
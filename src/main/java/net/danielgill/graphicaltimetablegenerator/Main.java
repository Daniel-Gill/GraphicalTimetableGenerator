package net.danielgill.graphicaltimetablegenerator;

import org.jfree.chart.ui.UIUtils;

public class Main{
    public static void main(String[] args){
        GraphGenerator demo = new GraphGenerator("Timetable");
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
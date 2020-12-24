package net.danielgill.graphicaltimetablegenerator;

import java.io.File;
import org.jfree.chart.ui.UIUtils;

public class Main{
    public static void main(String[] args){
        File tbfile = new File("C:/Users/dan23/Documents/NetBeansProjects/GraphicalTimetableGenerator/src/test/java/test.txt");
        GraphGenerator demo = new GraphGenerator("Timetable", tbfile);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
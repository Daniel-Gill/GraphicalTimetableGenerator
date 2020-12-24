package net.danielgill.graphicaltimetablegenerator;

import java.io.File;
import org.jfree.chart.ui.UIUtils;

public class Main{
    public static void main(String[] args){
        File xmlfile = new File("C:/Users/dan23/Documents/NetBeansProjects/GraphicalTimetableGenerator/src/test/java/testXML.xml");
        GraphGenerator demo = new GraphGenerator("Timetable", xmlfile);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
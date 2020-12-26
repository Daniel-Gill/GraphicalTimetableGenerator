package net.danielgill.graphicaltimetablegenerator;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.chart.ui.UIUtils;

public class Main{
    public static void main(String[] args){
        //File tbfile = new File("C:/Users/dan23/Documents/NetBeansProjects/GraphicalTimetableGenerator/src/test/java/test2.txt");
        File tbfile = processArgs(args);
        GraphGenerator demo = new GraphGenerator("Timetable", tbfile);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    
    public static File processArgs(String[] args) {
        CommandLineParser parser;
        Options options = new Options();
        options.addOption("i", true, "Input File");
        parser = new DefaultParser();
        
        try {
            CommandLine line = parser.parse(options, args);
            if(line.hasOption("i")) {
                String fileName = line.getOptionValue("i");
                if(fileName != null) {
                    File file = new File(fileName);
                    return file;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("File not specified.");
            System.exit(0);
            return null;
        }
        System.err.println("File not specified.");
        System.exit(0);
        return null;
    }
}
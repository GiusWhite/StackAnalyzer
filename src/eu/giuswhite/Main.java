package eu.giuswhite;

import eu.giuswhite.utils.CsvHelper;
import org.apache.commons.cli.*;

import java.util.Map;

public class Main {

    private static String toolFunction;
    private static String language;
    private static String stackoverflowDumpFilePath;

    public static void main(String[] args) {
        //Parsing arguments
       Main.processCommandLine(args);

        if (toolFunction != null) {
            switch (toolFunction) {
                case "parser":
                    if (stackoverflowDumpFilePath != null) {
                        ParserManager.getInstance().stackoverflowParser(Main.stackoverflowDumpFilePath);
                        Map map = CsvHelper.sortHashMapByKey(LineCounter.getInstance().getMapOfLinesStat("./"));
                        Map smallest = LineCounter.getInstance().getTopSmallestFile();
                        Map biggest = LineCounter.getInstance().getTopBiggestFile();
                        CsvHelper.writeHashMapToCsv("# of lines, # of posts", map, "stackoverflow_posts_loc_statistics");
                        CsvHelper.writeHashMapToCsv("Post ID, # of lines", smallest, "5_smallest_code");
                        CsvHelper.writeHashMapToCsv("Post ID, # of lines", biggest, "5_biggest_code");
                    } else {
                        System.out.println("Invalid path to Stackoverflow dump");
                    }
                    break;
                case "help":
                    Main.printShortDocumentation();
                    break;
            }
        } else {
            System.out.println("No tool function provided. Try with help for more info.");
        }

        //ParserManager.getInstance().simianLogsFilterParser();
        //ParserManager.getInstance().usefulSimianFragmentStatisticParser();
    }

    private static void processCommandLine(String[] args) {
        // create Options object
        Options options = new Options();

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // add t option
        options.addOption("stackparser", true, "Function of the tool");
//        options.addOption("f", "toolFunction", true, "Function of the tool");
//        options.addOption("i", "input", true, "Path to Stackoverflow dump");
//        options.addOption("s", "system", true, "Directory for output files");

        CommandLine cmd = null;
        System.out.println("Prima del try");
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cmd.hasOption("stackparser")) {
            if (cmd.getOptionValue("stackparser") != null) {
                Main.stackoverflowDumpFilePath = cmd.getOptionValue("stackparser");
                System.out.println(Main.stackoverflowDumpFilePath);
            }
        } else {
            System.out.println("I'm Here");
        }

    }


    private static void printShortDocumentation() {
        System.out.println("STACKOVERFLOW DUMP ANALYZER");
    }
}
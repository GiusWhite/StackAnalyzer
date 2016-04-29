package eu.giuswhite;

import eu.giuswhite.utils.CsvHelper;
import org.apache.commons.cli.*;

import java.util.Map;

public class Main {

    private static String stackoverflowDumpFilePath;

    public static void main(String[] args) {

        //Parsing arguments
        Main.processCommandLine(args);

        ParserManager.getInstance().stackoverflowParser(Main.stackoverflowDumpFilePath);
        Map map = CsvHelper.sortHashMapByKey(LineCounter.getInstance().getMapOfLinesStat("./"));
        Map smallest = LineCounter.getInstance().getTopSmallestFile();
        Map biggest = LineCounter.getInstance().getTopBiggestFile();
        CsvHelper.writeHashMapToCsv("# of lines, # of posts", map, "stackoverflow_posts_loc_statistics");
        CsvHelper.writeHashMapToCsv("Post ID, # of lines", smallest, "5_smallest_code");
        CsvHelper.writeHashMapToCsv("Post ID, # of lines", biggest, "5_biggest_code");

        //ParserManager.getInstance().simianLogsFilterParser();
        //ParserManager.getInstance().usefulSimianFragmentStatisticParser();
    }

    private static void processCommandLine(String[] args) {
        // create Options object
        Options options = new Options();

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // add t option
        options.addOption("i", "input", true, "Path to Stackoverflow dump");
        options.addOption("s", "system", true, "Directory for output files");
        options.addOption("h", "help", false, "Display help message.");

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("i")) {
                Main.stackoverflowDumpFilePath = cmd.getOptionValue("i");
            } else {
                throw new ParseException("No Stackoverflow dump path provided");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private static void printShortDocumentation() {
        System.out.println("STACKOVERFLOW DUMP ANALYZER");
        System.out.println("1st argument: tool mode");
        System.out.println("-stack: to retrieve code fragment from answers in stackoverflow dump posts file");
        System.out.println("2nd argument for -stack mode: stackoverflow posts dump file");
    }
}
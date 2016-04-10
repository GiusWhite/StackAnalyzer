package eu.giuswhite;

import eu.giuswhite.utils.CommonUtils;
import eu.giuswhite.utils.CsvHelper;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        if (args.length > 0) {
            if (args[0] != null) {
                if(args[0].equals("stack")){
                    if(args[1] != null){
                        String stackoverflowDumpFilePath = args[0];
                        ParserManager.getInstance().stackoverflowParser(stackoverflowDumpFilePath);
                        Map map = CsvHelper.sortHashMapByKey(LineCounter.getInstance().getMapOfLinesStat("./"));
                        Map smallest = LineCounter.getInstance().getTopSmallestFile();
                        Map biggest = LineCounter.getInstance().getTopBiggestFile();
                        CsvHelper.writeHashMapToCsv("# of lines, # of posts", map, "stackoverflow_posts_loc_statistics");
                        CsvHelper.writeHashMapToCsv("Post ID, # of lines", smallest, "5_smallest_code");
                        CsvHelper.writeHashMapToCsv("Post ID, # of lines", biggest, "5_biggest_code");
                    } else {
                        System.out.println("Stackoverflow posts file path is missing.");
                    }
                } else {
                    System.out.println("The selectedmode don't exists.");
                }
            }
        } else {
            Main.printShortDocumentation();
        }
        //ParserManager.getInstance().simianLogsFilterParser();
        //ParserManager.getInstance().usefulSimianFragmentStatisticParser();
    }

    private static void printShortDocumentation(){
        System.out.println("STACKOVERFLOW DUMP ANALYZER");
        System.out.println("1st argument: tool mode");
        System.out.println("-stack: to retrieve code fragment from answers in stackoverflow dump posts file");
        System.out.println("2nd argument for -stack mode: stackoverflow posts dump file");
    }
}
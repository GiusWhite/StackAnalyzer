package eu.giuswhite;

import eu.giuswhite.utils.CommonUtils;
import eu.giuswhite.utils.CsvHelper;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //ParserManager.getInstance().simianLogsFilterParser();
        ParserManager.getInstance().usefulSimianFragmentStatisticParser();
    }

    private static void printShortDocumentation(){
        System.out.println("STACKOVERFLOW DUMP ANALYZER");
        System.out.println("1st argument: tool mode");
        System.out.println("-stack: to retrieve code fragment from answers in stackoverflow dump posts file");
        System.out.println("2nd argument for -stack mode: stackoverflow posts dump file");
    }
}

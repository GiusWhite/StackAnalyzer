package eu.giuswhite;

public class Main {
    public static void main(String[] args) {
//        ParserManager.getInstance().stackoverflowParser();
//        Map map = LineCounter.getInstance().getMapOfLinesStat(CommonUtils.PROJECT_FOLDER_PATH + "\\data_four_split\\0\\");
//        Map smallest = LineCounter.getInstance().getTopSmallestFile();
//        Map biggest = LineCounter.getInstance().getTopBiggestFile();
//        CsvFileWriter.sortHashMapByKey("first_half_stats", CommonUtils.CODE_LINES_STAT_CSV_HEADER, map);
//        CsvFileWriter.writeHashMapToCsv("Post ID, # of lines", smallest, "first_half_5_smallest_code");
//        CsvFileWriter.writeHashMapToCsv("Post ID, # of lines", biggest, "first_half_5_sbiggest_code");

       /* Map map = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Path of projects folder:");
            String path = br.readLine();
            System.out.println("Destination path:");
            String destinatioPath = br.readLine();
            System.out.println("Result file name:");
            String resultFileName = br.readLine();
            FileManager.getDirectoryContents(new File(path), map);
            map = FileManager.randomizeMap(map);
            CsvFileWriter.writeHashMapToCsv(destinatioPath,"File Name, File Path", map, resultFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //ParserManager.getInstance().simianLogsFilterParser();
        ParserManager.getInstance().usefulSimianFragmentStatisticParser();
    }
}

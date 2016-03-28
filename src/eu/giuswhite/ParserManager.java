package eu.giuswhite;

import eu.giuswhite.beans.SimianLog;
import eu.giuswhite.beans.SimianStackoverflowFragment;
import eu.giuswhite.comparators.SimianLogComparator;
import eu.giuswhite.exceptions.SaxTerminatorException;
import eu.giuswhite.handlers.ParserHandler;
import eu.giuswhite.handlers.SimianLogsFilterHandler;
import eu.giuswhite.handlers.UsefulSimianFragmentHandler;
import eu.giuswhite.utils.CommonUtils;
import eu.giuswhite.utils.CsvFileWriter;
import eu.giuswhite.utils.FileManager;
import eu.giuswhite.utils.XmlFileWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by GiusWhite on 20/01/2016.
 */
public class ParserManager {
    public static final String FILE_PATH = "./data/Posts.xml";

    private static ParserManager instance = null;

    private SAXParser saxParser;
    private DefaultHandler handler;

    public static int NUMBER_OF_POSTS = 0;
    public static int NUMBER_OF_QUESTIONS = 0;
    public static int NUMBER_OF_ANSWERS = 0;
    public static int NUMBER_OF_JAVA_QUESTIONS = 0;
    public static int NUMBER_OF_JAVA_ANSWERS = 0;
    public static int NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS = 0;
    public static int NUMBER_OF_QUESTIONS_WITH_ANSWERS = 0;
    public static int TOTAL_LINE_NUMBER = 0;

    protected ParserManager() {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            this.saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static ParserManager getInstance() {
        if (instance == null) {
            instance = new ParserManager();
        }
        return instance;
    }

    public void stackoverflowParser() {
        try {
            this.handler = new ParserHandler();
            File xmlStackoverflowPosts = new File(ParserManager.FILE_PATH);
            this.saxParser.parse(xmlStackoverflowPosts, handler);
            FileManager.getInstance().createAndWriteFile(CommonUtils.PROJECT_FOLDER_PATH, "statistics_useful_code.txt", this.createStatisticString(), true);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
            FileManager.getInstance().createAndWriteFile(CommonUtils.PROJECT_FOLDER_PATH, "statistics_useful_code.txt", this.createStatisticString(), true);
        }
    }

    public void simianLogsFilterParser() {
        try {
            List<String> simianResults = new ArrayList<>();
            FileManager.getDirectoryContents(new File(CommonUtils.PROJECT_FOLDER_PATH + "cgf_converted_results"), simianResults, true);
            List<SimianLog> usefulFragment = new ArrayList<>();
            for (String fileName : simianResults) {
                this.handler = new SimianLogsFilterHandler(usefulFragment);
                File testFile = new File(CommonUtils.PROJECT_FOLDER_PATH + "cgf_converted_results\\" + fileName);
                this.saxParser.parse(testFile, handler);
            }
            //Only file name. Extension is added automatically
            XmlFileWriter.writeSimianUsefulFragmentsToXml("useful_fragments", usefulFragment);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
            FileManager.getInstance().createAndWriteFile(CommonUtils.PROJECT_FOLDER_PATH, "statistics_useful_code.txt", this.createStatisticString(), true);
        }
    }

    public void usefulSimianFragmentStatisticParser() {
        try {
            List<SimianLog> simianLogs = new ArrayList<>();
            File inputFile = new File(CommonUtils.PROJECT_FOLDER_PATH + "useful_fragments.xml");
            this.handler = new UsefulSimianFragmentHandler(simianLogs);
            this.saxParser.parse(inputFile, handler);
            List<SimianStackoverflowFragment> result = SimianLog.getSimianLogStats(simianLogs);
            CsvFileWriter.writeSimianLogsStatsOnCsv(result, "fragment_stats");
            CsvFileWriter.writeSimianLogsStatsOnCsv(SimianLog.sortByUsage(result), "fragment_stats_sorted_by_usage");
            CsvFileWriter.writeSimianLogsStatsOnCsv(SimianLog.sortByProjects(result), "fragment_stats_sorted_by_projects");
            CsvFileWriter.writeHashMapToCsv("Used, Number of Fragments", CsvFileWriter.sortHashMapByKey(SimianLog.getDistributionBy(result, SimianLogComparator.USAGE)),"fragment_distribution_by_usage");
            CsvFileWriter.writeHashMapToCsv("Used, Number of Fragments", CsvFileWriter.sortHashMapByKey(SimianLog.getDistributionBy(result, SimianLogComparator.PROJECTS)),"fragment_distribution_by_projects");
            CsvFileWriter.writeHashMapToCsv("No. of LOC, Occurrences", CsvFileWriter.sortHashMapByKey(SimianLog.getSimianLogLOCStats(simianLogs)),"fragment_loc_statistics");
            CsvFileWriter.writeHashMapToCsv("No. of LOC, Occurrences", CsvFileWriter.sortHashMapByKey(SimianLog.getSimianLogProjectsStats(simianLogs)),"fragment_project_statistics");

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
        }

    }

    private String createStatisticString() {
        String result = "";
        result += "NUMBER_OF_POSTS: " + NUMBER_OF_POSTS + "<br>";
        result += "NUMBER_OF_QUESTIONS: " + NUMBER_OF_QUESTIONS + "<br>";
        result += "NUMBER_OF_ANSWERS: " + NUMBER_OF_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_QUESTIONS: " + NUMBER_OF_JAVA_QUESTIONS + "<br>";
        result += "NUMBER_OF_QUESTIONS_WITH_ANSWERS: " + NUMBER_OF_QUESTIONS_WITH_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS: " + NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_ANSWERS: " + NUMBER_OF_JAVA_ANSWERS + "<br>";
        result += "TOTAL_NUMBER_OF_CODELINE: " + TOTAL_LINE_NUMBER;
        return result;
    }
}

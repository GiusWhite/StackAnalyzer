package eu.giuswhite;

import eu.giuswhite.beans.SimianLog;
import eu.giuswhite.beans.SimianStackoverflowFragment;
import eu.giuswhite.comparators.SimianLogComparator;
import eu.giuswhite.exceptions.SaxTerminatorException;
import eu.giuswhite.handlers.StackoverflowParserHandler;
import eu.giuswhite.handlers.SimianLogsFilterHandler;
import eu.giuswhite.handlers.UsefulSimianFragmentHandler;
import eu.giuswhite.utils.CommonUtils;
import eu.giuswhite.utils.CsvHelper;
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
import java.util.List;

/**
 * Created by GiusWhite on 20/01/2016.
 */

public class ParserManager {
    private static ParserManager instance = null;

    private SAXParser saxParser;
    private DefaultHandler handler;

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

    public void stackoverflowParser(String stackoverflowDumpPath) {
        try {
            this.handler = new StackoverflowParserHandler();
            File inputFile = new File(stackoverflowDumpPath);
            this.saxParser.parse(inputFile, handler);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
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
        }
    }

    public void usefulSimianFragmentStatisticParser() {
        try {
            List<SimianLog> simianLogs = new ArrayList<>();
            File inputFile = new File(CommonUtils.PROJECT_FOLDER_PATH + "useful_fragments.xml");
            this.handler = new UsefulSimianFragmentHandler(simianLogs);
            this.saxParser.parse(inputFile, handler);
            List<SimianStackoverflowFragment> result = SimianLog.getSimianLogStats(simianLogs);
            CsvHelper.writeSimianLogsStatsOnCsv(result, "fragment_stats");
            CsvHelper.writeSimianLogsStatsOnCsv(SimianLog.sortByUsage(result), "fragment_stats_sorted_by_usage");
            CsvHelper.writeSimianLogsStatsOnCsv(SimianLog.sortByProjects(result), "fragment_stats_sorted_by_projects");
            CsvHelper.writeHashMapToCsv("Used, Number of Fragments", CsvHelper.sortHashMapByKey(SimianLog.getDistributionBy(result, SimianLogComparator.USAGE)),"fragment_distribution_by_usage");
            CsvHelper.writeHashMapToCsv("Used, Number of Fragments", CsvHelper.sortHashMapByKey(SimianLog.getDistributionBy(result, SimianLogComparator.PROJECTS)),"fragment_distribution_by_projects");
            CsvHelper.writeHashMapToCsv("No. of LOC, Occurrences", CsvHelper.sortHashMapByKey(SimianLog.getSimianLogLOCStats(simianLogs)),"fragment_loc_statistics");
            CsvHelper.writeHashMapToCsv("No. of LOC, Occurrences", CsvHelper.sortHashMapByKey(SimianLog.getSimianLogProjectsStats(simianLogs)),"fragment_project_statistics");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (SaxTerminatorException allDone) {
            System.out.println("TERMINATED");
        }
    }
}

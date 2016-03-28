package eu.giuswhite.utils;

import eu.giuswhite.beans.SimianStackoverflowFragment;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by GiusWhite on 01/02/2016.
 */
public class CsvFileWriter {

    public static void writeHashMapToCsv(String header, Map<Object, Object> map, String filename) {
        StringWriter output = new StringWriter();
        try (ICsvListWriter listWriter = new CsvListWriter(output,
                CsvPreference.STANDARD_PREFERENCE)) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                listWriter.write(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = header + "\n" + output.toString();
        FileManager.getInstance().createAndWriteFile(CommonUtils.PROJECT_FOLDER_PATH, filename + ".csv", result, false);
    }

    public static void writeHashMapToCsv(String destinationPath, String header, Map<?, ?> map, String filename) {
        StringWriter output = new StringWriter();
        try (ICsvListWriter listWriter = new CsvListWriter(output,
                CsvPreference.STANDARD_PREFERENCE)) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                listWriter.write(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = header + "\n" + output.toString();
        FileManager.getInstance().createAndWriteFile(destinationPath, filename + ".csv", result, false);
    }

    public static Map<Object, Object> sortHashMapByKey(Map<Object, Object> unsortedMap) {
        return new TreeMap<>(unsortedMap);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static void writeSimianLogsStatsOnCsv(List<SimianStackoverflowFragment> list, String filename) {
        StringWriter output = new StringWriter();
        try (ICsvListWriter listWriter = new CsvListWriter(output,
                CsvPreference.STANDARD_PREFERENCE)) {
            for (SimianStackoverflowFragment fragment : list) {
                listWriter.write(fragment.fragmentName, fragment.numberOfTimeIsUsed, fragment.projectsWhereIsUsed.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "Fragment Name, Used, In Projects\n" + output.toString();
        FileManager.getInstance().createAndWriteFile(CommonUtils.PROJECT_FOLDER_PATH, filename + ".csv", result, false);
    }
}

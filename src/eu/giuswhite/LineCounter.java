package eu.giuswhite;

import eu.giuswhite.utils.CommonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GiusWhite on 01/02/2016.
 */
public class LineCounter {

    public static LineCounter instance = null;
    public Map<String, Integer> topSmallestCode = new HashMap<>();
    public Map<String, Integer> topBiggestCode = new HashMap<>();

    protected LineCounter() {
    }

    public static LineCounter getInstance() {
        if (instance == null) {
            instance = new LineCounter();
        }
        return instance;
    }

    public int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public Map getMapOfLinesStat(String folderPath) {
        Map<Integer, Integer> lineStatMap = new HashMap<>();
        File dir = new File(folderPath);
        List<String> allFilesInFolder = new ArrayList<>();
        this.getDirectoryContents(dir, allFilesInFolder);
        for (String file : allFilesInFolder) {
            try {
                int numberOfLines = this.countLines(file);
                ParserManager.TOTAL_LINE_NUMBER = ParserManager.TOTAL_LINE_NUMBER + numberOfLines;
                this.manageTopFile(file, numberOfLines);
                if (lineStatMap.containsKey(numberOfLines)) {
                    lineStatMap.put(numberOfLines, lineStatMap.get(numberOfLines) + 1);
                } else {
                    lineStatMap.put(numberOfLines, 1);
                }
                System.out.println("DONE: " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineStatMap;
    }

    private void getDirectoryContents(File dir, List<String> filesName) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getDirectoryContents(file, filesName);
            } else {
                filesName.add(file.getAbsolutePath());
            }
        }
        System.out.println("SIZE: " + filesName.size());
    }

    public int getNumberOfLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    public void manageTopFile(String filename, int numberOfLines) {
        if (this.topBiggestCode.size() < 5) {
            this.topBiggestCode.put(filename, numberOfLines);
        } else if (this.topSmallestCode.size() < 5) {
            this.topSmallestCode.put(filename, numberOfLines);
        } else {
            int lineCounter = -1;
            String key = "";
            manageSmallestTopFive(filename, numberOfLines, key, lineCounter);
            manageBiggestTopFive(filename, numberOfLines, key, lineCounter);
        }
    }

    public void manageSmallestTopFive(String filename, int numberOfLines, String key, int minumumLine) {
        for (Map.Entry<String, Integer> entry : this.topSmallestCode.entrySet()) {
            if (minumumLine == -1) {
                minumumLine = entry.getValue();
                key = entry.getKey();
            } else {
                if (entry.getValue() > minumumLine) {
                    minumumLine = entry.getValue();
                    key = entry.getKey();
                }
            }
        }
        if (numberOfLines < minumumLine) {
            this.topSmallestCode.remove(key);
            this.topSmallestCode.put(filename, numberOfLines);
        }
    }

    public void manageBiggestTopFive(String filename, int numberOfLines, String key, int minumumLine) {
        for (Map.Entry<String, Integer> entry : this.topBiggestCode.entrySet()) {
            if (minumumLine == -1) {
                minumumLine = entry.getValue();
                key = entry.getKey();
            } else {
                if (entry.getValue() < minumumLine) {
                    minumumLine = entry.getValue();
                    key = entry.getKey();
                }
            }
        }
        if (numberOfLines > minumumLine) {
            this.topBiggestCode.remove(key);
            this.topBiggestCode.put(filename, numberOfLines);
        }
    }

    public Map getTopBiggestFile() {
        return this.topBiggestCode;
    }

    public Map getTopSmallestFile() {
        return this.topSmallestCode;
    }
}

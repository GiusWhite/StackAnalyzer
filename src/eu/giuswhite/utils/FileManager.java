package eu.giuswhite.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by GiusWhite on 21/01/2016.
 */
public class FileManager {
    private static FileManager instance = null;

    protected FileManager() {
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void createAndWriteFile(String path, String fileName, String body, boolean reformatCode) {
        File file = new File(path + fileName);
        try {
            if (!Files.exists(Paths.get(path))) {
                File f = new File(path);
                f.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (!reformatCode) {
                this.writeFormattedBody(body, bufferedWriter);
            } else {
                String[] formattedBodyStrings = this.splitBody(body);
                this.writeFormattedString(formattedBodyStrings, bufferedWriter);
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] splitBody(String inlineCode) {
        String[] result = inlineCode.split("<br>");
        return result;
    }

    private void writeFormattedString(String[] inlineCode, BufferedWriter bufferedWriter) throws IOException {
        for (String s : inlineCode) {
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }
    }

    public void writeFormattedBody(String inlineCode, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(inlineCode);
    }

    public static void splitFolderContent(String folderPath, String newFolderPath, int numberOfSubGroup) {
        if (!Files.exists(Paths.get(newFolderPath))) {
            File f = new File(newFolderPath);
            f.mkdir();
        }
        int counter = 0;
        List<String> folderContent = new ArrayList<>();
        File file = new File(folderPath);
        FileManager.getDirectoryContents(file, folderContent);
        try {
            for (String f : folderContent
                    ) {
                File source = new File(f);
                if (counter < numberOfSubGroup-1) {
                    FileUtils.copyFileToDirectory(source, new File(newFolderPath+"\\"+counter+"\\"));
                    counter++;
                } else {
                    FileUtils.copyFileToDirectory(source, new File(newFolderPath+"\\"+counter+"\\"));
                    counter = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void getDirectoryContents(File dir, List<String> filesName) {
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

    public static void getDirectoryContents(File dir, List<String> filesName, boolean onlyName) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getDirectoryContents(file, filesName);
            } else {
                if(onlyName){
                    filesName.add(file.getName());
                }
            }
        }
        System.out.println("SIZE: " + filesName.size());
    }

    public static void getDirectoryContents(File dir, Map<String, String> filesName){
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getDirectoryContents(file, filesName);
            } else {
                filesName.put(file.getName(), file.getAbsolutePath());
            }
        }
        System.out.println("SIZE: " + filesName.size());
    }

    public static Map<String, String> randomizeMap(Map <String, String> map){
        Map<String, String> result = new HashMap<>();
        Random ran = new Random();
        List<String> keys = new ArrayList<>(map.keySet());
        while (!keys.isEmpty()){
            int randomIndex = ran.nextInt(keys.size());
            String randomKey = keys.get(randomIndex);
            String randomItem = map.get(randomKey);
            result.put(randomKey, randomItem);
            keys.remove(randomIndex);
        }
        return result;
    }
}

package eu.giuswhite.beans;

import eu.giuswhite.comparators.SimianLogComparator;

import java.util.*;

/**
 * Created by GiusWhite on 15/03/2016.
 */
public class SimianLog {
    public int id;
    public List<LogFragment> fragmentList;

    public SimianLog() {
        this.fragmentList = new ArrayList<>();
    }

    public LogFragment getEmptyFragment() {
        this.fragmentList.add(new LogFragment());
        return this.fragmentList.get(this.fragmentList.size() - 1);
    }

    public boolean isUseful() {
        boolean result = false;
        boolean thereIsStackOverflowFile = false;
        boolean thereIsSrcFile = false;
        for (LogFragment logFragment : this.fragmentList
                ) {
            if (logFragment.getFilePath().contains("/extracted_data_full/")) {
                thereIsStackOverflowFile = true;
            } else if (logFragment.getFilePath().contains("/src/")) {
                thereIsSrcFile = true;
            }
        }
        result = thereIsSrcFile && thereIsStackOverflowFile;
        return result;
    }

    @Override
    public String toString() {
        return "SimianLog{" +
                "id=" + id +
                ", fragmentList=" + fragmentList +
                '}';
    }

    public static List<SimianStackoverflowFragment> getSimianLogStats(List<SimianLog> simianLogs){
        List<SimianStackoverflowFragment> result = new ArrayList<>();
        for(SimianLog s : simianLogs){
            for(LogFragment lF : s.fragmentList){
                /*Controllo se esiste nella lista risultato.*/
                if(lF.isStackoverflowFragment){
                    boolean exist = false;
                    SimianStackoverflowFragment simianStackoverflowFragment = new SimianStackoverflowFragment();
                    for (SimianStackoverflowFragment sSF : result){
                        if(sSF.fragmentName.equals(lF.filePath)){
                            exist = true;
                            simianStackoverflowFragment = sSF;
                            break;
                        }
                    }
                    /* Se non esiste, lo creo e lo aggiungo*/
                    if(!exist){
                        simianStackoverflowFragment.fragmentName = lF.filePath;
                        result.add(simianStackoverflowFragment);
                    }
                    for(LogFragment lF2: s.fragmentList){
                        if(!lF2.isStackoverflowFragment){
                            simianStackoverflowFragment.numberOfTimeIsUsed++;
                            exist = false;
                            for (String projectName: simianStackoverflowFragment.projectsWhereIsUsed){
                                if(projectName.equals(lF2.filePath)){
                                    exist = true;
                                }
                            }
                            if(!exist){
                                simianStackoverflowFragment.projectsWhereIsUsed.add(lF2.filePath);
                            }
                        }
                    }
                }
            }

        }
        return result;
    }

    public static List<SimianStackoverflowFragment> sortByUsage(List<SimianStackoverflowFragment> list){
        List<SimianStackoverflowFragment> result = list;
        Collections.sort(result, new SimianLogComparator(SimianLogComparator.USAGE));
        return result;
    }

    public static List<SimianStackoverflowFragment> sortByProjects(List<SimianStackoverflowFragment> list){
        List<SimianStackoverflowFragment> result = list;
        Collections.sort(result, new SimianLogComparator(SimianLogComparator.PROJECTS));
        return result;
    }

    public static Map<Object, Object> getDistributionBy(List<SimianStackoverflowFragment> list, int by){
        Map<Object,Object> result = new HashMap<>();
        for (SimianStackoverflowFragment s: list) {
            if(by == SimianLogComparator.PROJECTS){
                int projectsNumber = s.projectsWhereIsUsed.size();
                if(result.containsKey(projectsNumber)){
                    int current = (int) result.get(projectsNumber);
                    result.put(projectsNumber, current+1);
                } else {
                    result.put(projectsNumber, 1);
                }
            } else {
                int projectsNumber = s.numberOfTimeIsUsed;
                if(result.containsKey(projectsNumber)){
                    int current = (int) result.get(projectsNumber);
                    result.put(projectsNumber, current+1);
                } else {
                    result.put(projectsNumber, 1);
                }
            }

        }
        return result;
    }

    public static Map<Object, Object> getSimianLogLOCStats(List<SimianLog> simianLogs){
        Map<Object, Object> result = new HashMap<>();
        for(SimianLog s : simianLogs){
            for(LogFragment lF : s.fragmentList){
               if(lF.isStackoverflowFragment){
                   if(result.containsKey(lF.getNumberOfUsedLOC())){
                       int current = (int) result.get(lF.getNumberOfUsedLOC());
                       result.put(lF.getNumberOfUsedLOC(), current+1);
                   } else {
                       result.put(lF.getNumberOfUsedLOC(), 1);
                   }
               }
            }

        }
        return result;
    }

    public static Map<Object, Object> getSimianLogProjectsStats(List<SimianLog> simianLogs){
        Map<Object, Object> result = new HashMap<>();
        for(SimianLog s : simianLogs){
            for(LogFragment lF : s.fragmentList){
                if(!lF.isStackoverflowFragment){
                    if(result.containsKey(lF.filePath)){
                        int current = (int) result.get(lF.filePath);
                        result.put(lF.filePath, current+1);
                    } else {
                        result.put(lF.filePath, 1);
                    }
                }
            }

        }
        return result;
    }

    public class LogFragment {
        public String filePath;
        public int start;
        public int end;
        public boolean isStackoverflowFragment;

        public LogFragment() {
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getNumberOfUsedLOC(){
            return this.end-this.start+1;
        }
        @Override
        public String toString() {
            return "LogFragment{" +
                    "filePath='" + filePath + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}

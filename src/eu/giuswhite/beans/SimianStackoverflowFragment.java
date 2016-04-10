package eu.giuswhite.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by GiusWhite on 22/03/2016.
 */
public class SimianStackoverflowFragment implements Comparable<SimianStackoverflowFragment> {
    public String fragmentName;
    public int numberOfTimeIsUsed;
    public int totalLOC;
    public double percentageOfReusedLoc;
    public List<Integer> reusedLines;
    public List<String> projectsWhereIsUsed;

    public SimianStackoverflowFragment() {
        this.numberOfTimeIsUsed = 0;
        this.totalLOC = 0;
        this.projectsWhereIsUsed = new ArrayList<>();
        this.reusedLines = new ArrayList<>();
    }

    public static void orderFragmentsByPercentageOfReusedLoc(List<SimianStackoverflowFragment> unsortedList, boolean reverse) {
        Collections.sort(unsortedList);
        if(reverse){
            Collections.reverse(unsortedList);
        }
    }

    @Override
    public int compareTo(SimianStackoverflowFragment o) {
        return Double.compare(this.percentageOfReusedLoc, o.percentageOfReusedLoc);
    }
}

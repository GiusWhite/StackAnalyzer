package eu.giuswhite.comparators;

import eu.giuswhite.beans.SimianStackoverflowFragment;

import java.util.Comparator;

/**
 * Created by GiusWhite on 25/03/2016.
 */
public class SimianLogComparator implements Comparator<SimianStackoverflowFragment> {

    public static int USAGE = 1;
    public static int PROJECTS = 0;
    private int sortBy;

    public SimianLogComparator(int sortBy){
        this.sortBy = sortBy;
    }

    @Override
    public int compare(SimianStackoverflowFragment o1, SimianStackoverflowFragment o2) {
        if(this.sortBy == USAGE){
            return compare(o1.numberOfTimeIsUsed, o2.numberOfTimeIsUsed);
        } else {
            return compare(o1.projectsWhereIsUsed.size(), o2.projectsWhereIsUsed.size());
        }
    }

    private static int compare(int a, int b) {
        return a < b ? -1
                : a > b ? 1
                : 0;
    }
}

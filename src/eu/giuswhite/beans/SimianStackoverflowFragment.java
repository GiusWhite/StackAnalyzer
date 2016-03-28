package eu.giuswhite.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiusWhite on 22/03/2016.
 */
public class SimianStackoverflowFragment {
    public String fragmentName;
    public int numberOfTimeIsUsed;
    public List<String> projectsWhereIsUsed;

    public SimianStackoverflowFragment(){
        this.numberOfTimeIsUsed = 0;
        this.projectsWhereIsUsed = new ArrayList<>();
    }
}

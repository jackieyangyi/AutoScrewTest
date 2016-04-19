package com.autodoor.autoscrew.ActSetScrew;

import java.util.Comparator;

/**
 * Created by yangyi on 2016/2/3.
 */
public class ModelComparator implements Comparator {

    boolean flag = true;

    public ModelComparator(boolean f) {
        flag = f;
    }

    @Override
    public int compare(Object lhs, Object rhs) {

        Model model1 = (Model) lhs;
        Model model2 = (Model) rhs;
        if (flag)
            return new Integer(model2.getY()).compareTo(new Integer(model1.getY()));
        else
            return new Integer(model1.getY()).compareTo(new Integer(model2.getY()));

    }
}

package com.skytech.moa.widgets.indelible;

import com.skytech.moa.model.Contact;

import java.util.Comparator;

public class PinyinComparator implements Comparator<IIndelibleModel> {

    public int compare(IIndelibleModel o1, IIndelibleModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}

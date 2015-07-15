package com.skytech.moa.utils;

import com.skytech.moa.model.Document;
import com.skytech.moa.model.DocumentDetail;

import java.util.*;

public class DocTimeSort {
    public static class sortClass implements Comparator {
        public int compare(Object arg0, Object arg1) {
            DocumentDetail docDetail0 = (DocumentDetail)arg0;
            DocumentDetail docDetail1 = (DocumentDetail)arg1;
            int flag = docDetail0.getReleaseTime().compareTo(docDetail1.getReleaseTime());
            return flag;
        }
    }

    public static ArrayList<DocumentDetail> docDetailsTimeSort(ArrayList<DocumentDetail> documentDetails) {
        sortClass sort = new sortClass();
        Collections.sort(documentDetails, sort);
        return documentDetails;
    }
}

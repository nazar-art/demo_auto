package com.epam.dp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataProviderHelper extends BaseDP {

    private DataProviderHelper() {
    }

    public static synchronized Object[][] toObject(Object obj) {
        return new Object[][]{new Object[]{obj}};
    }

    public static synchronized Iterator<Object[]> toListObject(List items) {
        List<Object[]> data = new ArrayList<Object[]>();
        for (Object item : items) {
            data.add(new Object[]{item});
        }
        return data.iterator();
    }
}

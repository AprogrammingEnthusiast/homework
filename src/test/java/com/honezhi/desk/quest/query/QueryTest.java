package com.honezhi.desk.quest.query;

import com.honezhi.desk.quest.construction.ConstructionTest;

public class QueryTest {

    public static void main(String[] args) {
        ConstructionTest.construction();
        query("13000020001");
    }

    public static void query(String mobile) {
        int prefix = Integer.parseInt(mobile.substring(0, 7));
        int code = ConstructionTest.prefixArray[ConstructionTest.getPrefixBin(prefix)];
        String name = ConstructionTest.nameArray[code];
        System.out.println(name);
    }
}

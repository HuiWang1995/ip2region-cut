package org.lionsoul.ip2region.area;

import cn.hutool.core.io.FileUtil;

public class YunaiAreaCountryTest {

    public static void main(String[] args) {
        int i = 0;
        String raw = FileUtil.readUtf8String("/Users/yunai/Java/ip2region/data/global_region.csv");
        for (String str : raw.split("\n")) {
            String[] strs = str.split(",");
            if (!strs[1].equals("0")) {
                continue;
            }
            i++;
            System.out.println(i + "\t" + strs[2] + "\t" + "1" + "\t" + "0");
        }
    }
}

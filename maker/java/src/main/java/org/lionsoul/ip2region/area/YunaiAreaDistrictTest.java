package org.lionsoul.ip2region.area;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

public class YunaiAreaDistrictTest {

    public static void main(String[] args) {
        String raw = FileUtil.readUtf8String("/Users/yunai/Java/ip2region/data/area/district.csv");
        raw = raw.replace("\"", "");
        for (String str : raw.split("\n")) {
            String[] strs = str.split(",");
            String id = strs[0];
            String parentId = strs[2] + "00";
            if (parentId.equals("500200")) { // 特殊逻辑：重庆下，都要归到“重庆市”；因为它都归到重庆市；参考淘宝！
                parentId = "500100";
            }
            String name = strs[1];
            System.out.println(id + "\t" + name + "\t" + "4" + "\t" + parentId);
        }
    }
}

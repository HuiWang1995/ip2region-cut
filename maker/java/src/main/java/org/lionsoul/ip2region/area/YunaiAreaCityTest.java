package org.lionsoul.ip2region.area;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

public class YunaiAreaCityTest {

    public static void main(String[] args) {
        String raw = FileUtil.readUtf8String("/Users/yunai/Java/ip2region/data/area/city.csv");
        raw = raw.replace("\"", "");
        for (String str : raw.split("\n")) {
            String[] strs = str.split(",");
            String id = strs[0] + "00";
            String parentId = strs[2] + "0000";
            String name = strs[1];
            if (id.equals("500200")) { // 特殊逻辑：重庆下，有个“县”要跳过；因为它都归到重庆市；参考淘宝！
                continue;
            }
            if (id.equals("110100")) {
                name = "北京市";
            } else if (id.equals("120100")) {
                name = "天津市";
            } else if (id.equals("310100")) {
                name = "上海市";
            } else if (id.equals("500100")) {
                name = "重庆市";
            }
            Assert.notEquals("市辖区", name);
            System.out.println(id + "\t" + name + "\t" + "3" + "\t" + parentId);
        }
    }
}

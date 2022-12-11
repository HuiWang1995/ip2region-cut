package org.lionsoul.ip2region.area;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

public class YunaiAreaProvinceTest {

    public static void main(String[] args) {
        String raw = FileUtil.readUtf8String("/Users/yunai/Java/ip2region/data/area/province.csv");
        raw = raw.replace("\"", "");
        for (String str : raw.split("\n")) {
            String[] strs = str.split(",");
            String id = strs[0] + "0000";
            String name = strs[1];
            if (StrUtil.equalsAny(name, "北京市", "上海市", "天津市", "重庆市")) {
                name = name.substring(0, name.length() - 1);
            }
            System.out.println(id + "\t" + name + "\t" + "2" + "\t" + "1");
        }
    }
}

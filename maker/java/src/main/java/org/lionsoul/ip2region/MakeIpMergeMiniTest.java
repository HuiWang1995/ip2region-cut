package org.lionsoul.ip2region;

import org.lionsoul.ip2region.xdb.Log;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MakeIpMergeMiniTest {
    private static final Log log = Log.getLogger(MakeIpMergeMiniTest.class);

    private static final String countryFile = "/home/dark/IdeaProjects/ip2region/data/global_region.csv";

    private static String provinceFile = "/home/dark/IdeaProjects/ip2region/data/provinces.csv";

    private static String cityFile = "/home/dark/IdeaProjects/ip2region/data/cities.csv";

    private static String ipMergeSrc = "/home/dark/IdeaProjects/ip2region/data/ip.merge.txt";

    private static String ipMergeOut = "/home/dark/IdeaProjects/ip2region/data/ip.merge.txt.mini";

    private static String ipMergeErr = "/home/dark/IdeaProjects/ip2region/data/error.txt.";

    /**
     * 国家码值Map,key为国家名称，value为国家的编码
     * 码值映射来自于global_region.csv前200多行
     * <a href="https://github.com/lionsoul2014/ip2region/blob/master/data/global_region.csv"/>
     * 1,0,中国,1,0
     * 2,0,蒙古,1,0
     */
    private static Map<String, String> countryMap = new HashMap<String, String>(255);

    /**
     * 省份映射码值
     * key为中文名称，value为省码值
     *
     * <a href="https://github.com/modood/Administrative-divisions-of-China/blob/master/dist/provinces.csv"/>
     * code,name
     * 11,"北京市"
     * 12,"天津市"
     */
    private static Map<String, String> provinceMap = new HashMap<String, String>(31);

    /**
     * 城市映射码值
     * key为中文名称，value为城市码值
     * <a href="https://github.com/modood/Administrative-divisions-of-China/blob/master/dist/cities.csv"/>
     */
    private static Map<String, String> cityMap = new HashMap<String, String>(360);

    private static String destFileStr = null;


    public static void main(String[] args) throws Exception {
        initCountry();
        initProvince();
        initCity();
        readAndWriteMiniTxt();
        // 参数传递给MakerTest
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            String arg = args[i];
            if (arg.startsWith("--src=")) {
                args[i] = "--src=" + destFileStr;
                break;
            }
        }
        // --dst 不做处理
        MakerTest.main(args);


    }

    private static void initCountry() throws Exception {
        // 后续参数化
        File srcFile = new File(countryFile);
        String line;
        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("utf-8")));
        while ((line = br.readLine()) != null) {
            final String[] ps = line.split(",");
            if (!"1".equals(ps[3])) {
                // 不是国家级
                continue;
            }
            countryMap.put(ps[2], ps[0]);
        }
        br.close();
        fis.close();
    }

    private static void initProvince() throws Exception {
        // 后续参数化
        File srcFile = new File(provinceFile);
        String line;
        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("utf-8")));
        // 舍弃第一行
        br.readLine();
        while ((line = br.readLine()) != null) {
            final String[] ps = line.split(",");
            provinceMap.put(ps[1].substring(1, ps[1].length() - 1), ps[0]);
        }
        br.close();
        fis.close();
        // 内蒙古自治区
        provinceMap.put("内蒙古", "15");
        // 广西壮族自治区
        provinceMap.put("广西", "45");
        // 西藏自治区
        provinceMap.put("西藏", "54");
        // 宁夏回族自治区
        provinceMap.put("宁夏", "64");
        // 新疆维吾尔自治区
        provinceMap.put("新疆", "65");
        // todo 香港、台湾、澳门还未知
        provinceMap.put("台湾省", "0");
        provinceMap.put("香港", "0");
        provinceMap.put("澳门", "0");
    }

    private static void initCity() throws Exception {
        // 后续参数化
        File srcFile = new File(cityFile);
        String line;
        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("utf-8")));
        // 舍弃第一行
        br.readLine();
        while ((line = br.readLine()) != null) {
            final String[] ps = line.split(",");
            cityMap.put(ps[1].substring(1, ps[1].length() - 1), ps[0]);
        }
        br.close();
        fis.close();
    }


    private static void readAndWriteMiniTxt() throws Exception {
        log.infof("try to load the segments ... ");
        // 相对路径 -- 后续参数化
        File srcFile = new File(ipMergeSrc);
        // 后续参数化
        File destFile = new File(ipMergeOut);
        destFileStr = destFile.getAbsolutePath();
        // 后续参数化
        File errFile = new File(ipMergeErr);
        String line;

        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("utf-8")));
        final FileOutputStream fos = new FileOutputStream(destFile);
        final FileOutputStream eos = new FileOutputStream(errFile);
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("utf-8")));
        final BufferedWriter ew = new BufferedWriter(new OutputStreamWriter(eos, Charset.forName("utf-8")));
        final String china = "中国";
        final String zero = "0";
        while ((line = br.readLine()) != null) {
            final String[] ps = line.split("\\|");
            String code = null;
            // 目前特殊地区“欧洲”，“阿联酋”等不存在
            // 从国家到省到市
            String tmpCode;
            boolean isChina = false;
            if (!china.equals(ps[2])) {
                // 中国判断
                String country = ps[2];
                tmpCode = countryMap.get(country);
                if (tmpCode == null) {
                    // 孟加拉 -> 孟加拉国 是存在的
                    tmpCode = countryMap.get(country + "国");
                }
                code = tmpCode;
            } else {
                // 中国码值
//                code = "1";
                isChina = true;
            }
            if (isChina) {
                // 判断省是否存在
                String province = ps[4];
                if (!zero.equals(province)) {
                    // 省份判断
                    tmpCode = provinceMap.get(province);
                    if (null != tmpCode) {
                        code = tmpCode;
                    } else {
                        // 可能是直辖市
                        tmpCode = provinceMap.get(province + "市");
                        if (null != tmpCode) {
                            code = tmpCode;
                        } else {
                            // 有些没加省
                            tmpCode = provinceMap.get(province + "省");
                            if (null != tmpCode) {
                                code = tmpCode;
                            } else {
                                // 现在就剩 台湾省、香港、内蒙古、广西
                                // 算了，我手动加到map里面吧
                                ew.write(line);
                                ew.newLine();
                                System.out.println("找不到省：" + province);
                            }
                        }
                    }
                }
                String city = ps[5];
                if (!zero.equals(city)) {
                    // 城市判断
                    tmpCode = cityMap.get(city);
                    if (null != tmpCode) {
                        code = tmpCode;
                    } else {
                        // 如果省有了，市没有，可能是直辖市。例如“北京市”、“上海市”，直接列为“市辖区”即可
                        if (null != code) {
                            code += "01";
//                            System.out.println("找不到市：" + city);
                        }
                    }
                }
            }
            if (code == null && isChina) {
                // 中国
                code = "1";
            }
            if (code == null) {
                code = zero;
                ew.write(line);
                ew.newLine();
//                System.out.println("找不到码值：" + line);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(ps[0]).append('|')
                    .append(ps[1]).append('|')
                    .append(code);
            String newLine = sb.toString();
//            System.out.println(newLine);
            bw.write(newLine);
            bw.newLine();
        }
        br.close();
        bw.flush();
        bw.close();
        ew.flush();
        ew.close();
    }
}

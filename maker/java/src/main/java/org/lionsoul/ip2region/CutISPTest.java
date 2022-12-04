package org.lionsoul.ip2region;

import org.lionsoul.ip2region.xdb.Log;

import java.io.*;
import java.nio.charset.Charset;

public class CutISPTest {
    private static final Log log = Log.getLogger(CutISPTest.class);

    public static void main(String[] args) throws Exception {

        log.infof("try to load the segments ... ");
        // 相对路径 -- 后续参数化
        File srcFile = new File("/home/dark/IdeaProjects/ip2region/data/ip.merge.txt");
        // 后续参数化
        File destFile = new File("/home/dark/IdeaProjects/ip2region/data/ip.merge.txt."
                + System.currentTimeMillis());
        String line;

        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("utf-8")));
        final FileOutputStream fos = new FileOutputStream(destFile);
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("utf-8")));
        while ((line = br.readLine()) != null) {
            final String[] ps = line.split("\\|");
            StringBuilder sb = new StringBuilder();
            // 最后一个 ISP不要了
            for (int i = 0; i < ps.length - 1; i++) {
                sb.append(ps[i]).append('|');
            }
            sb.deleteCharAt(sb.length() - 1);
//            sb.append('\n');
            String newLine = sb.toString();
            bw.write(newLine);
            bw.newLine();
        }
        br.close();
        bw.flush();
        bw.close();

    }
}

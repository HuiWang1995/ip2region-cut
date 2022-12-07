package org.lionsoul.ip2region;

import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class YunaiSearcherTest {

    public static void main(String[] args) throws IOException {
        // 1、创建 searcher 对象
        String dbPath = "/Users/yunai/Java/ip2region/data/ip2region-test.xdb";
//        String dbPath = "/Users/yunai/Java/ip2region/data/ip2region.xdb";
        Searcher searcher = null;
        try {
            byte[] vIndex = Searcher.loadVectorIndexFromFile(dbPath);
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
            return;
        }

        // 2、查询
//        String ip = "1.2.3.4";
        String ip = "1.0.171.0";
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源
        searcher.close();

        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
    }

}

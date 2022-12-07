// Copyright 2022 The Ip2Region Authors. All rights reserved.
// Use of this source code is governed by a Apache2.0-style
// license that can be found in the LICENSE file.
//
// @Author Lion <chenxin619315@gmail.com>
// @Date   2022/07/12

package org.lionsoul.ip2region;

import org.lionsoul.ip2region.xdb.IndexPolicy;
import org.lionsoul.ip2region.xdb.Log;
import org.lionsoul.ip2region.xdb.Maker;

public class YunaiMakerTest {

    public static void main(String[] args) throws Exception {
        String srcFile = "/Users/yunai/Java/ip2region/data/ip.test.txt";
        String dstFile = "/Users/yunai/Java/ip2region/data/ip2region-test.xdb";
        int indexPolicy = IndexPolicy.Vector;
        Maker maker = new Maker(indexPolicy, srcFile, dstFile);
        maker.init();
        maker.start();
        maker.end();
    }

}

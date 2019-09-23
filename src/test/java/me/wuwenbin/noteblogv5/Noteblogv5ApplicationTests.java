package me.wuwenbin.noteblogv5;

import cn.hutool.core.img.ImgUtil;
import org.junit.Test;

import java.awt.*;
import java.io.File;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Noteblogv5ApplicationTests {

    @Test
    public void contextLoads() {
//        ImgUtil.cut(
//                new File("E:\\upload\\noteblogv5\\img\\2019-09-11\\39fb0f85-2b57-41a3-9411-1b829b1c1a63.jpg"),
//                new File("E:\\upload\\noteblogv5\\img\\2019-09-11\\1.jpg"),
//                new Rectangle(0, 0, 200, 150)
//        );
        ImgUtil.scale(
                new File("E:\\upload\\noteblogv5\\img\\2019-09-11\\39fb0f85-2b57-41a3-9411-1b829b1c1a63.jpg"),
                new File("E:\\upload\\noteblogv5\\img\\2019-09-11\\39fb0f85-2b57-41a3-9411-1b829b1c1a63.jpg"),
                400, 300, Color.WHITE
        );
    }

    @Test
    public void mockData(){

    }

}

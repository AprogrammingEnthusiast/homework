package com.honezhi.desk.quest.area;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.honezhi.desk.quest.QuestApp;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuestApp.class)
public class AreaServiceTest {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AreaServiceTest.class);

    @Autowired
    private AreaService areaService;

    @Test
    public void getArea() {
        assertNull(areaService.getArea("12900012333"));

        assertEquals(1300002, areaService.getArea("13000020001").getPrefix());
        assertEquals(393, areaService.getArea("13000020001").getCode());
        assertEquals("测试地区.393", areaService.getArea("13000020001").getName());

        assertEquals(1969742, areaService.getArea("19697420001").getPrefix());
        assertEquals(2, areaService.getArea("19697420001").getCode());
        assertEquals("广东.深圳", areaService.getArea("19697420001").getName());
    }

    @Test
    public void benchmark() {
        Random r = new Random();
        int limit = 100000;
        String[] phones = new String[limit];
        for (int i = 0; i < phones.length; i++) {
            String phone = "13" + String.format("%09d", r.nextInt(999999999));
            phones[i] = phone;
        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < phones.length; i++) {
            areaService.getArea(phones[i]);
        }
        long time = System.currentTimeMillis() - t1;
        log.info("time cost:{},  tps:{}", time, ((float) phones.length) / time * 1000);
    }

    @Test
    public void memoryUsage() {
        long NameArray = ObjectSizeCalculator.getObjectSize(AreaService.getNameArray()) / 1024;
        log.info("NameArray memory:{} KB", NameArray);
        long PrefixArray = ObjectSizeCalculator.getObjectSize(AreaService.getPrefixArray()) / 1024 / 1024;
        log.info("PrefixArray memory:{} M", PrefixArray);
    }

}
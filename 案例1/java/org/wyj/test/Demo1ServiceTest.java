package org.wyj.test;

import java.util.List;
import org.junit.Test;
import org.wyj.entity.demo1DO;
import org.wyj.service.IDemo1Service;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Blob;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * @author wuyaojun
 * @date 2024/7/3
 */
public class Demo1ServiceTest {

    @Autowired
    private IDemo1Service demo1Service;

    @Test
    public void testInsert() {
        demo1DO demo1DO = new demo1DO();
        demo1DO.setId(1);
        demo1DO.setATinyint();
        demo1DO.setASmallint();
        demo1DO.setAMediumint(1);
        demo1DO.setAInt(1);
        demo1DO.setAInteger(1);
        demo1DO.setABigint(1L);
        demo1DO.setABoolean();
        demo1DO.setABit();
        demo1DO.setAFloat();
        demo1DO.setADouble(1.0);
        demo1DO.setADecimal();
        demo1DO.setAChar("a");
        demo1DO.setAVarchar("a");
        demo1DO.setATinytext("a");
        demo1DO.setAText("a");
        demo1DO.setAMediumtext("a");
        demo1DO.setALongText("a");
        demo1DO.setADate(new Date());
        demo1DO.setATime(new Date());
        demo1DO.setADatetime(new Date());
        demo1DO.setATimestamp(new Date());
        demo1DO.setAYear(new Date());
        demo1DO.setABlob();
        Integer id = demo1Service.create(demo1DO);
        assertTrue(id > 0);
    }

    @Test
    public void testSelectAll() {
        List<demo1DO> demo1DOList = demo1Service.selectAll();
        assertTrue(demo1DOList != null && demo1DOList.size() > 0);
    }

    @Test
    public void testSelectById() {
        demo1DO demo1DO = demo1Service.selectById(1);
        assertNotNull(demo1DO);
    }

    @Test
    public void testUpdate() {
        demo1DO demo1DO = new demo1DO();
        demo1DO.setId(1);
        demo1DO.setATinyint();
        demo1DO.setASmallint();
        demo1DO.setAMediumint(1);
        demo1DO.setAInt(1);
        demo1DO.setAInteger(1);
        demo1DO.setABigint(1L);
        demo1DO.setABoolean();
        demo1DO.setABit();
        demo1DO.setAFloat();
        demo1DO.setADouble(1.0);
        demo1DO.setADecimal();
        demo1DO.setAChar("a");
        demo1DO.setAVarchar("a");
        demo1DO.setATinytext("a");
        demo1DO.setAText("a");
        demo1DO.setAMediumtext("a");
        demo1DO.setALongText("a");
        demo1DO.setADate(new Date());
        demo1DO.setATime(new Date());
        demo1DO.setADatetime(new Date());
        demo1DO.setATimestamp(new Date());
        demo1DO.setAYear(new Date());
        demo1DO.setABlob();
        int i = demo1Service.update(demo1DO);
        assertEquals(1, i);
    }

    @Test
    public void testDelete() {
        int i = demo1Service.delete(1);
        assertEquals(1, i);
    }
}

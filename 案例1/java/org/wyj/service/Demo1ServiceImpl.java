package org.wyj.service;

import java.util.List;
import org.wyj.entity.demo1DO;
import org.wyj.mapper.Demo1Mapper;
import org.wyj.service.IDemo1Service;

/**
 * @author wuyaojun
 * @date 2024/7/3
 */
public class Demo1ServiceImpl implements IDemo1Service {

    @Autowired
    private Demo1Mapper demo1Mapper;

    @Override
    public List<demo1DO> selectAll() {
        return demo1Mapper.selectList();
    }

    @Override
    public Integer create(demo1DO demo1DO) {
        demo1Mapper.insert(demo1DO);
        return demo1DO.getId();
    }

    @Override
    public demo1DO selectById(Integer id) {
        return demo1Mapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(demo1DO demo1DO) {
        return demo1Mapper.updateByPrimaryKey(demo1DO);
    }

    @Override
    public int delete(Integer id) {
        return demo1Mapper.deleteByPrimaryKey(id);
    }
}

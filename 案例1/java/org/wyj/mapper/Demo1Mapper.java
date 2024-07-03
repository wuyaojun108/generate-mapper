package org.wyj.mapper;

import java.util.List;
import org.wyj.entity.demo1DO;

/**
 * @author wuyaojun
 * @date 2024/7/3
 *
 * 
 */
public interface Demo1Mapper {
    List<demo1DO> selectList();

    int insert(demo1DO demo1DO);

    demo1DO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(demo1DO demo1DO);

    int deleteByPrimaryKey(Integer id);
}

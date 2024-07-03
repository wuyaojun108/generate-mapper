package org.wyj.service;

import java.util.List;
import org.wyj.entity.demo1DO;

/**
 * @author wuyaojun
 * @date 2024/7/3
 */
public interface IDemo1Service {
    List<demo1DO> selectAll();

    Integer create(demo1DO demo1DO);

    demo1DO selectById(Integer id);

    int update(demo1DO demo1DO);

    int delete(Integer id);
}

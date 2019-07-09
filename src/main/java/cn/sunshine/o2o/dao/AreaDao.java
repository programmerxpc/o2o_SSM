package cn.sunshine.o2o.dao;

import cn.sunshine.o2o.entity.Area;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 22:13
 */
public interface AreaDao {

    /**
     * 获取区域列表
     * @return areaList
     */
    List<Area> queryArea();

}

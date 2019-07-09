package cn.sunshine.o2o.service;

import cn.sunshine.o2o.entity.Area;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 22:42
 */
public interface AreaService {

    /**
     * 获取区域列表
     * @return areaList
     */
    List<Area> getAreaList();

}

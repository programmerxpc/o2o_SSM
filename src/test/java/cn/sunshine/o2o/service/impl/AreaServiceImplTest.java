package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.BaseTest;
import cn.sunshine.o2o.entity.Area;
import cn.sunshine.o2o.service.AreaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 22:47
 */
public class AreaServiceImplTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void getAreaList() {
        List<Area> areaList = areaService.getAreaList();
        assertEquals("西苑",areaList.get(0).getAreaName());
    }
}
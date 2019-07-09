package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.dao.AreaDao;
import cn.sunshine.o2o.entity.Area;
import cn.sunshine.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 22:44
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}

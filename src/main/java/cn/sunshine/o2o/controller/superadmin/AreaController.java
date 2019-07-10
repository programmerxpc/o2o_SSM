package cn.sunshine.o2o.controller.superadmin;

import cn.sunshine.o2o.entity.Area;
import cn.sunshine.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 22:53
 */
@RestController
@RequestMapping("/superadmin/*")
public class AreaController {

    Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @GetMapping("listarea")
    private Map<String,Object> listArea(){
        logger.info("===start===");
        long startTime = System.currentTimeMillis();
        Map<String,Object> modelMap = new HashMap<>();
        try {
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("success",true);
            modelMap.put("areaList",areaList);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        logger.error("test error!");
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]",endTime - startTime);
        logger.info("===end===");
        return modelMap;
    }

}

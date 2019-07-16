package cn.sunshine.o2o.controller.frontend;

import cn.sunshine.o2o.entity.HeadLine;
import cn.sunshine.o2o.entity.ShopCategory;
import cn.sunshine.o2o.service.HeadLineService;
import cn.sunshine.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiaoPengCheng
 * @create 2019-07-15 19:10
 */
@RestController
@RequestMapping("/frontend/*")
public class MainPageController {

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private HeadLineService headLineService;

    @GetMapping("listmainpageinfo")
    private Map<String,Object> listMainPageInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            //获取一级店铺类别列表（即parentId为空的shopCategory）
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategory(null);
            //获取状态为可用（1）的头条列表
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            List<HeadLine> headLineList = headLineService.getHeadLineList(headLineCondition);
            modelMap.put("success",true);
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("headLineList",headLineList);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
        }
        return modelMap;
    }

}

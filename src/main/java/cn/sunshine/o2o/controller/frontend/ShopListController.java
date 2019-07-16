package cn.sunshine.o2o.controller.frontend;

import cn.sunshine.o2o.dto.ShopExecution;
import cn.sunshine.o2o.entity.Area;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.entity.ShopCategory;
import cn.sunshine.o2o.enums.ShopStateEnum;
import cn.sunshine.o2o.service.AreaService;
import cn.sunshine.o2o.service.ShopCategoryService;
import cn.sunshine.o2o.service.ShopService;
import cn.sunshine.o2o.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiaoPengCheng
 * @create 2019-07-16 10:25
 */
@RestController
@RequestMapping("/frontend/*")
public class ShopListController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;

    //返回商品列表页里的ShopCategory列表(二级或一级)，以及区域信息列表
    @GetMapping("listshopspageinfo")
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1){
            try {
                //若parentId存在，则取出该一级Shopcategory下的的二级Shopcategory列表
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategory(shopCategoryCondition);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            try {
                //如果parentId不存在，则取出所有一级shopCategory(用户在首页选择的是全部商店列表)
                shopCategoryList = shopCategoryService.getShopCategory(null);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        try {
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
    }

    //获取指定查询条件下的店铺列表
    @GetMapping("listshops")
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取一页的显示数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if ((pageIndex > -1) && (pageSize > -1)){
            //尝试获取一级类别id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //尝试获取特定二级类别id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //尝试获取区域id
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            //尝试获取模糊查询名字
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            //获取组合之后的查询条件
            Shop shopCondition = compactShopCondition(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageIndex or pageSize");
        }
        return modelMap;
    }

    private Shop compactShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId > -1L){
            //查询某个一级ShopCategory下的所有二级shopCategory的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId > -1L){
            //查询某个二级shopCategory下的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId > -1){
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName != null){
            shopCondition.setShopName(shopName);
        }
        //前端展示系统都是审核成功的店铺
        shopCondition.setEnableStatus(ShopStateEnum.PASS.getState());
        return shopCondition;
    }

}

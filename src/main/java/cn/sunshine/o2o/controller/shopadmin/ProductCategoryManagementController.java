package cn.sunshine.o2o.controller.shopadmin;

import cn.sunshine.o2o.dto.ProductCategoryExecution;
import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.enums.ProductCategoryStateEnum;
import cn.sunshine.o2o.exceptions.ProductCategoryOperationException;
import cn.sunshine.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 9:42
 */
@RestController
@RequestMapping("/shopadmin/*")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("removeproductcategory")
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if (productCategoryId != null && productCategoryId > 0){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                if (currentShop != null && currentShop.getShopId() > 0){
                    ProductCategoryExecution pce = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                    if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                        modelMap.put("success",true);
                    }else {
                        modelMap.put("success",false);
                        modelMap.put("errMsg",pce.getStateInfo());
                    }
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productCategoryId");
        }
        return modelMap;
    }

    @PostMapping("addproductcategories")
    private Map<String,Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop != null && currentShop.getShopId() > 0){
            if (productCategoryList != null && productCategoryList.size() > 0){
                for (ProductCategory pc : productCategoryList){
                    pc.setShopId(currentShop.getShopId());
                }
                try {
                    ProductCategoryExecution pce = productCategoryService.batchAddProductCategory(productCategoryList);
                    if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()){//操作成功
                        modelMap.put("success",true);
                    }else {
                        modelMap.put("success",false);
                        modelMap.put("errMsg",pce.getStateInfo());
                    }
                } catch (ProductCategoryOperationException e) {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",e.toString());
                    return modelMap;
                }
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","empty productCategoryList");
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg",ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
        }
        return modelMap;
    }

    @GetMapping("getproductcategorylist")
    private Map<String,Object> getProductCategoryList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop != null && currentShop.getShopId() > 0){
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            modelMap.put("success",true);
            modelMap.put("productCategoryList",productCategoryList);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg",ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
        }
        return modelMap;
    }

}

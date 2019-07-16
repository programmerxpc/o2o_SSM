package cn.sunshine.o2o.controller.frontend;

import cn.sunshine.o2o.dto.ProductExecution;
import cn.sunshine.o2o.entity.Product;
import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.enums.ProductStateEnum;
import cn.sunshine.o2o.service.ProductCategoryService;
import cn.sunshine.o2o.service.ProductService;
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
 * @create 2019-07-16 16:52
 */
@RestController
@RequestMapping("/frontend/*")
public class ShopDetailController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    //根据查询条件分页列出该店铺下的所有商品
    @GetMapping("listproductsbyshop")
    private Map<String,Object> listProductsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取一页展示的条数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取店铺id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)){
            //尝试获取productCategoryId
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            //尝试获取模糊查询的商品名
            String productName = HttpServletRequestUtil.getString(request, "productName");
            //组合查询条件
            Product productCondition = compactProductCondition(shopId,productCategoryId,productName);
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId > -1L){
            //按productCategoryId查询某个店铺下的所有商品
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null){
            //按productName模糊查询某个店铺下的所有商品
            productCondition.setProductName(productName);
        }
        //只允许查找状态为上架的商品
        productCondition.setEnableStatus(ProductStateEnum.SHOW.getState());
        return productCondition;
    }

    //获取店铺信息以及该店铺下的商品类别列表
    @GetMapping("listshopdetailpageinfo")
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取前台传过来的shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId != -1){
            //获取店铺id为shopId的店铺信息
            Shop shop = shopService.getByShopId(shopId);
            //获取店铺下面的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

}

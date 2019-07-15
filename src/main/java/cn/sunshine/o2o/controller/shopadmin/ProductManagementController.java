package cn.sunshine.o2o.controller.shopadmin;

import cn.sunshine.o2o.dto.ImageHolder;
import cn.sunshine.o2o.dto.ProductExecution;
import cn.sunshine.o2o.entity.Product;
import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.enums.ProductStateEnum;
import cn.sunshine.o2o.exceptions.ProductOperationException;
import cn.sunshine.o2o.service.ProductCategoryService;
import cn.sunshine.o2o.service.ProductService;
import cn.sunshine.o2o.utils.CodeUtil;
import cn.sunshine.o2o.utils.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiaoPengCheng
 * @create 2019-07-14 11:53
 */
@RestController
@RequestMapping("/shopadmin/*")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传商品详情图的最大数量
    private static final int IMAGEMAXCOUNT = 6;

    @GetMapping("getproductlistbyshop")
    private Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取前台传入的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //获取前台传入的每页需要显示的商品数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session中获取店铺信息，主要获取shopId
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)){
            //获取传入的需要检索的条件,包括是否需要从，商品类别,模糊查找商品名,商品状态，去筛选商品类别列表
            //筛选的条件可以排列组合
            String productName = HttpServletRequestUtil.getString(request, "productName");
            int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productName,enableStatus,productCategoryId);
            //传入查询条件以及分页信息进行查询
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success",true);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","传入参数为空");
        }
        return modelMap;
    }

    /**
     * 封装商品查询条件
     * @param shopId
     * @param productName
     * @param enableStatus
     * @param productCategoryId
     * @return
     */
    private Product compactProductCondition(Long shopId, String productName, int enableStatus, long productCategoryId) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //若有商品名要求，则添加进去
        if (productName != null){
            productCondition.setProductName(productName);
        }
        //若有商品状态要求，则添加
        if (enableStatus > -1){
            productCondition.setEnableStatus(enableStatus);
        }
        //若有商品类别要求，则添加
        if (productCategoryId > -1){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        return productCondition;
    }

    @PostMapping("modifyproduct")
    private Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //是商品编辑时候调用还是上下架操作的时候调用
        //若为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数，包括商品信息，缩略图和商品详情图列表
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if (commonsMultipartResolver.isMultipart(request)){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if (thumbnailFile != null){
                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
                }
                //取出详情图列表并构建list<ImageHolder>列表对象，最多支持六张图片上传
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productImgFile != null){
                        //若取出的第i个详情图片文件流不为空，则将其加入详情图列表
                        productImgList.add(new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream()));
                    }else {
                        //若取出的第i个详情图片文件流为空，则终止循环
                        break;
                    }
                }
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
            //获取前端传来的字符串并转换成Product实体类
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if (product != null){
            try {
                //从session中获取当前店铺id并赋值给product
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //更新商品信息
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (ProductStateEnum.SUCCESS.getState() == pe.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品信息不能为空!");
        }
        return modelMap;
    }

    @GetMapping("getproductbyid")
    private Map<String,Object> getProductById(@RequestParam Long productId){
        Map<String,Object> modelMap = new HashMap<>();
        if (productId != null && productId > 0){
            //获取商品信息
            Product product = productService.getProductById(productId);
            //获取该店铺的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("success",true);
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }

    @PostMapping("addproduct")
    private Map<String,Object> addProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数，包括商品信息，缩略图和商品详情图列表
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if (commonsMultipartResolver.isMultipart(request)){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
                //取出详情图列表并构建list<ImageHolder>列表对象，最多支持六张图片上传
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productImgFile != null){
                        //若取出的第i个详情图片文件流不为空，则将其加入详情图列表
                        productImgList.add(new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream()));
                    }else {
                        //若取出的第i个详情图片文件流为空，则终止循环
                        break;
                    }
                }
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
            //获取前端传来的字符串并转换成Product实体类
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //若商品信息，缩略图和详情图列表都不为空，则进行商品添加
        if (product != null && thumbnail != null && productImgList.size() > 0){
            try {
                //从session中获取当前店铺id并赋值给product
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品信息和图片不能为空");
        }
        return modelMap;
    }

}

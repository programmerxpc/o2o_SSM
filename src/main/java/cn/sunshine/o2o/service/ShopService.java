package cn.sunshine.o2o.service;

import cn.sunshine.o2o.dto.ShopExecution;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 23:01
 */
public interface ShopService {

    /**
     * 通过shopCondition分页返回店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;

    /**
     * 通过shopId获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 添加店铺,包括对图片的处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;

}

package cn.sunshine.o2o.service;

import cn.sunshine.o2o.dto.ShopExecution;
import cn.sunshine.o2o.entity.Shop;

import java.io.File;
import java.io.InputStream;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 23:01
 */
public interface ShopService {

    /**
     * 添加店铺
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName);

}

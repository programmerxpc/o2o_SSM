package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.dao.ShopDao;
import cn.sunshine.o2o.dto.ShopExecution;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.enums.ShopStateEnum;
import cn.sunshine.o2o.exceptions.ShopOperationException;
import cn.sunshine.o2o.service.ShopService;
import cn.sunshine.o2o.utils.ImageUtil;
import cn.sunshine.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 23:02
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        //空值判断
        if (shop == null)
            return new ShopExecution(ShopStateEnum.EMPTY);
        try {
            shop.setEnableStatus(ShopStateEnum.CHECKING.getState());
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0){
                throw new ShopOperationException("店铺创建失败");
            }else {
                if (shopImgInputStream != null){
                    //存储图片
                    try {
                        addShopImg(shop,shopImgInputStream,fileName);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error" + e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.SUCCESS,shop);
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,fileName,dest);
        shop.setShopImg(shopImgAddr);
    }
}

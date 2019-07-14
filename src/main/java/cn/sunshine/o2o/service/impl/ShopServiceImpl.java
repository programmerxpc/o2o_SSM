package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.dao.ShopDao;
import cn.sunshine.o2o.dto.ImageHolder;
import cn.sunshine.o2o.dto.ShopExecution;
import cn.sunshine.o2o.entity.Shop;
import cn.sunshine.o2o.enums.ShopStateEnum;
import cn.sunshine.o2o.exceptions.ShopOperationException;
import cn.sunshine.o2o.service.ShopService;
import cn.sunshine.o2o.utils.ImageUtil;
import cn.sunshine.o2o.utils.PageCalculator;
import cn.sunshine.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 23:02
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null && shopList.size() > 0){
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
        return se;
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.EMPTY);
        }else {
            try {
                //1.判断是否需要处理图片
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null){//如果原先图片存在
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop,thumbnail);
                }
                //2.更新店铺信息
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error:" + e.getMessage());
            }
        }
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        //空值判断
        if (shop == null)
            return new ShopExecution(ShopStateEnum.EMPTY);
        try {
            shop.setEnableStatus(ShopStateEnum.CHECKING.getState());
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0){
                throw new ShopOperationException("店铺创建失败");
            }else {
                if (thumbnail.getImage() != null){
                    //存储图片
                    try {
                        addShopImg(shop,thumbnail);
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

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        shop.setShopImg(shopImgAddr);
    }
}

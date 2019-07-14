package cn.sunshine.o2o.service.impl;

import cn.sunshine.o2o.dao.ProductDao;
import cn.sunshine.o2o.dao.ProductImgDao;
import cn.sunshine.o2o.dto.ImageHolder;
import cn.sunshine.o2o.dto.ProductExecution;
import cn.sunshine.o2o.entity.Product;
import cn.sunshine.o2o.entity.ProductImg;
import cn.sunshine.o2o.enums.ProductStateEnum;
import cn.sunshine.o2o.exceptions.ProductOperationException;
import cn.sunshine.o2o.service.ProductService;
import cn.sunshine.o2o.utils.ImageUtil;
import cn.sunshine.o2o.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-14 10:42
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    //1.若缩略图参数有值，则处理缩略图
    //若原先存在缩略图，则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    //2.若商品详情图列表参数有值，对若商品详情图列表进行相同操作
    //3.将tb_product_img下的该商品原先的商品详情图记录全部清除
    //4.更新tb_product_img和tb_product的信息
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //若商品缩略图参数不为空且原来的缩略图不为空则删除原有缩略图并添加
            if (thumbnail != null){
                //先获取原有信息，看是否有原图片
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            //若详情图参数不为空,将原来的图片删除，并添加新的图片
            if (imageHolderList != null && imageHolderList.size() > 0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,imageHolderList);
            }
            try {
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0){
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            } catch (ProductOperationException e) {
                throw new ProductOperationException("updateProduct error:" + e.getMessage());
            }
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 删除某个商品下的所有详情图
     * @param productId
     */
    private void deleteProductImgList(Long productId) {
        //根据productId获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgByProductId(productId);
        //删除原来的图片
        for (ProductImg productImg : productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库中原图片的图片地址
        productImgDao.deleteProductImgByProductId(productId);
    }

    @Override
    @Transactional
    //1.处理缩略图,获取图片的相对路径并赋值给product
    //2.往tb_product写入商品信息，并获取productId
    //3.结合productId批量处理商品详情图
    //4.将商品详情图列表插入tb_product_img中
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //默认上架状态
            product.setEnableStatus(ProductStateEnum.SHOW.getState());
            //处理缩略图
            if (thumbnail != null){
                addThumbnail(product,thumbnail);
            }
            try {
                //往tb_product写入商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0){
                    throw new ProductOperationException("商品添加失败");
                }
            } catch (ProductOperationException e) {
                throw new ProductOperationException("insertProduct error:" + e.getMessage());
            }
            //结合productId批量处理商品详情图
            if (imageHolderList != null && imageHolderList.size() > 0){
                addProductImgList(product,imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 批量添加图片
     * @param product
     * @param imageHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) {
        //获取图片存储路径，这里直接存放到相应店铺的文件夹下
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        //遍历图片一次去处理，并添加进productImg实体类里
        for (ImageHolder imageHolder : imageHolderList){
            String imgAddr = ImageUtil.generateNormalImg(imageHolder,dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImgList.add(productImg);
        }
        try {
            //批量添加图片
            int effectedNum = productImgDao.batchInsertProductImg(productImgList);
            if (effectedNum <= 0){
                throw new ProductOperationException("批量添加商品详情图失败");
            }
        } catch (ProductOperationException e) {
            throw new ProductOperationException("batchInsertProductImg error:" + e.getMessage());
        }
    }

    /**
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail,dest);
        product.setImgAddr(thumbnailAddr);
    }
}

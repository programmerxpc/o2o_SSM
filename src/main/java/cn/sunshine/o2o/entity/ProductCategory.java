package cn.sunshine.o2o.entity;

import java.util.Date;

/**
 * @author XiaoPengCheng
 * @create 2019-07-09 18:03
 *
 * 商品类别
 */
public class ProductCategory {

    /**
     * ID
     */
    private Long productCategoryId;

    /**
     * 商品类别名称
     */
    private String productCategoryName;

    private Integer priority;

    private Date createTime;

    private Long shopId;

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "productCategoryId=" + productCategoryId +
                ", productCategoryName='" + productCategoryName + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", shopId=" + shopId +
                '}';
    }
}

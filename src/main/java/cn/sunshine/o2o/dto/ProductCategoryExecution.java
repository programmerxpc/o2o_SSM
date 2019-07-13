package cn.sunshine.o2o.dto;

import cn.sunshine.o2o.entity.ProductCategory;
import cn.sunshine.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 11:43
 */
public class ProductCategoryExecution {

    private int state;

    private String stateInfo;

    private List<ProductCategory> productCategoryList;

    public ProductCategoryExecution(){

    }

    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,List<ProductCategory> productCategoryList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}

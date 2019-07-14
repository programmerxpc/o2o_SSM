package cn.sunshine.o2o.enums;

/**
 * @author XiaoPengCheng
 * @create 2019-07-13 23:56
 */
public enum ProductStateEnum {

    UNUSEABLE(0,"下架"),
    SHOW(1,"上架"),
    INNER_ERROR(-1001,"内部系统错误"),
    SUCCESS(2,"操作成功"),
    EMPTY(-1002,"传入参数为空")
    ;

    private int state;

    private String stateInfo;

    ProductStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state返回相应的enum值
     * @param state
     * @return
     */
    public static ProductStateEnum stateOf(int state){
        for (ProductStateEnum stateEnum : values()){
            if (stateEnum.getState() == state)
                return stateEnum;
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}

package cn.sunshine.o2o.enums;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 22:47
 */
public enum ShopStateEnum {

    CHECKING(0,"审核中"),
    OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),
    PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),
    EMPTY(-1002,"传入参数为空");

    private int state;

    private String stateInfo;

    ShopStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state返回相应的enum值
     * @param state
     * @return
     */
    public static ShopStateEnum stateOf(int state){
        for (ShopStateEnum stateEnum : values()){
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

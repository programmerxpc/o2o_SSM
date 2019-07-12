package cn.sunshine.o2o.utils;

/**
 * @author XiaoPengCheng
 * @create 2019-07-12 19:06
 */
public class PageCalculator {

    /**
     *将页码转化为数据库行码
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @return
     */
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }

}

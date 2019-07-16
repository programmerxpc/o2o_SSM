package cn.sunshine.o2o.utils;

/**
 * @author XiaoPengCheng
 * @create 2019-07-10 22:19
 */
public class PathUtil {

    private static String separator = System.getProperty("file.separator");

    /**
     * 返回项目图片根路径
     * @return
     */
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")){
            basePath = "D:/projectdev/image";
        }else {
            basePath = "/Users/o2o/image";
        }
        basePath = basePath.replace("/",separator);
        return basePath;
    }

    /**
     * 返回项目图片子路径
     * @param shopId
     * @return
     */
    public static String getShopImagePath(long shopId){
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/",separator);
    }

}

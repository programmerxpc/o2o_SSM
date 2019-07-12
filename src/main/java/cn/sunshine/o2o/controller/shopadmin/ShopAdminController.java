package cn.sunshine.o2o.controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XiaoPengCheng
 * @create 2019-07-11 10:07
 *
 * 店铺页面控制
 */
@Controller
@RequestMapping("/shopadmin/*")
public class ShopAdminController {

    /**
     * 店铺添加和编辑页面
     * @return
     */
    @GetMapping("shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }

    /**
     * 店铺列表页面
     * @return
     */
    @GetMapping("shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    /**
     * 商店管理页面
     * @return
     */
    @GetMapping("shopmanagement")
    public String shopManagement(){
        return "shop/shopmanagement";
    }

}

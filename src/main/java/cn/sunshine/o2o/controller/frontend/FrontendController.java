package cn.sunshine.o2o.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XiaoPengCheng
 * @create 2019-07-15 19:32
 */
@Controller
@RequestMapping("/frontend/*")
public class FrontendController {

    //主页
    @GetMapping("index")
    public String index(){
        return "frontend/index";
    }

    //商品列表页
    @GetMapping("shoplist")
    public String shopList(){
        return "frontend/shoplist";
    }

    //店铺详情页
    @GetMapping("shopdetail")
    public String shopDetail(){
        return "frontend/shopdetail";
    }
}

package com.pack;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * author: cjianquan
 * date: 2017/3/17
 */
@Controller
@RequestMapping("/orderController")
public class OrderController {

    @RequestMapping(value = "/goOrderForm")
    public String goOrderForm(HttpServletRequest request){
        return "test1";
    }
}

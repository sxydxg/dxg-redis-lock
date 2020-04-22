package dxg.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 类说明
 *
 * @author dingxigui
 * @date 2020/4/22
 */
@RestController
public class TestController {


    @RequestMapping("/dxg/hello")
    public String hello(){
        return new Date().toString() ;
    }
}

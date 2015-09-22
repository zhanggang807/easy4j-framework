package easy4j.framework.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-18
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public ModelAndView index(HttpServletRequest request ,HttpServletResponse response) throws Exception{

        return new ModelAndView("index.jsp");

    }

    @RequestMapping(value = "/vm")
    public String indexVm(HttpServletRequest request ,HttpServletResponse response) throws Exception{

        return "index";

    }


}

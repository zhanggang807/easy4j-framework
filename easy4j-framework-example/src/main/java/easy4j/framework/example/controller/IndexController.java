package easy4j.framework.example.controller;

import com.google.common.collect.Maps;
import easy4j.framework.example.dao.IndexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-18
 */
@Controller
public class IndexController {

    @Autowired
    private IndexDao indexDao;

    @RequestMapping(value = "/")
     public String indexVm(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        Object ret = indexDao.save("sdsf");
        return "index";

    }

    @RequestMapping(value = "/chong")
    public String indexChong(HttpServletRequest request ,HttpServletResponse response) throws Exception{

        return "chongzhi/jd/com/index";

    }

    @RequestMapping(value = "/vm")
    public String webInf(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        return request.getParameter("vm");
    }


    @RequestMapping(value = "/json")
    public @ResponseBody Map json(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        Map map = Maps.newHashMap();
        map.put("code",1);
        return map;

    }

    @RequestMapping(value = "/str")
    public @ResponseBody String  str(HttpServletRequest request ,HttpServletResponse response) throws Exception{
        return "this is string 你好";

    }

    @RequestMapping(value = "/model")
    public Model json(HttpServletRequest request ,HttpServletResponse response,Model model) throws Exception{
        model.addAttribute("code",1);
        return model;

    }


    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }
}

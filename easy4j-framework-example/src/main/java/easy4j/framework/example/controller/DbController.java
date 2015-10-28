package easy4j.framework.example.controller;

import easy4j.framework.example.dao.IndexDao;
import org.easy4j.framework.core.orm.EntityMapping;
import org.easy4j.framework.core.orm.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * GET /db/{tablename} query list from the table of tablename
 *
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-27
 */
@Controller
@RequestMapping(value = "/db")
public class DbController {

    @Autowired
    private IndexDao indexDao ;

    @RequestMapping(value = "/{tableName}" ,method = RequestMethod.GET )
    public void queryList(@PathVariable("tableName") String tableName
            ,HttpServletRequest request){


    }

    public IndexDao getIndexDao() {
        return indexDao;
    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }

    private Map<String,Object> parseRequest(HttpServletRequest request,String tableName){

        Mapping mapping = EntityMapping.getMapping(tableName);

        for(String column : mapping.getColumns()){
            String val = request.getParameter(column);

        }

        return null ;
    }
}

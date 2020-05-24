package com.wangfulin.sparkstreamingweb.dao;

import com.wangfulin.sparkstreamingweb.domain.CourseClickCount;
import com.wangfulin.sparkstreamingweb.utils.HBaseUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @projectName: sparkstreamingweb
 * @description: 实战课程访问数量数据访问层
 * @author: Wangfulin
 * @create: 2020-05-24 13:17
 **/
@Component
public class CourseClickCountDAO {
    /**
     * 根据天查询
     */
    public List<CourseClickCount> query(String day) throws Exception {

        List<CourseClickCount> list = new ArrayList<>();

        // 去HBase表中根据day获取实战课程对应的访问量
        Map<String, Long> map = HBaseUtils.getInstance().query("course_clickcount", day);

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            CourseClickCount model = new CourseClickCount();
            model.setName(entry.getKey());
            model.setValue(entry.getValue());

            list.add(model);
        }

        return list;
    }

    public static void main(String[] args) throws Exception {
        CourseClickCountDAO dao = new CourseClickCountDAO();
        List<CourseClickCount> list = dao.query("20200524");
        for (CourseClickCount model : list) {
            System.out.println(model.getName() + " : " + model.getValue());
        }
    }
}

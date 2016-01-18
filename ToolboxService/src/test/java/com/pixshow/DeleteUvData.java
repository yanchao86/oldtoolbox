package com.pixshow;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.pixshow.framework.support.BaseDao;

@Component
public class DeleteUvData extends BaseDao {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/framework/spring.xml");
        DeleteUvData test = context.getBean(DeleteUvData.class);
        test.delete();
    }

    private void delete() {
//        int m = getJdbcTemplate().queryForInt("select count(*) from tb_toolbox_day_stat_uv WHERE `day`=?", 20150302);
//        System.out.println(m);
        update("insert into tb_toolbox_day_stat_uv_new_copy select * from tb_toolbox_day_stat_uv_new WHERE `day`=?", 20150302);
    }
}

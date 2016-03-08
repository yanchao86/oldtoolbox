package com.pixshow.toolboxmgr.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pixshow.framework.support.BaseDao;
import com.pixshow.toolboxmgr.bean.UserBean;

@Repository
public class UserDao extends BaseDao {

    public void createUserAppCode(String appCode) {
        List<Map<String, Object>> show = getJdbcTemplate().queryForList("SHOW TABLES LIKE 'user_"+appCode+"'");
        if(show.isEmpty()) {
            update("create table user_" + appCode + " like user");
        }
    }

    public int addUser(UserBean user, String appCode) {
        return insertBean("user_" + appCode, user).intValue();
    }

    public void updateUser(UserBean user, String appCode) {
        updateBean("user_" + appCode, user, "id=" + user.getId());
    }

    public UserBean findByMac(String mac, String appCode) {
        return queryForBean("select * from user_" + appCode + " where mac=?", UserBean.class, mac);
    }

    public List<UserBean> findByArea(int area, String appCode) {
        if (area == 1) { //国内
            return queryForList("select * from user_" + appCode + " where lang like ?", UserBean.class, "zh%");
        } else if (area == 2) {//国外
            return queryForList("select * from user_" + appCode + " where lang not like ?", UserBean.class, "zh%");
        } else {
            return queryForList("select * from user_" + appCode, UserBean.class);
        }
    }

    public List<UserBean> findByArea(int area, String appCode, String appVersion) {
        if (area == 1) { //国内
            return queryForList("select * from user_" + appCode + " where lang like ? and appVersion = ?", UserBean.class, "zh%", appVersion);
        } else if (area == 2) {//国外
            return queryForList("select * from user_" + appCode + " where lang not like ? and appVersion = ?", UserBean.class, "zh%", appVersion);
        } else {
            return queryForList("select * from user_" + appCode + " where appVersion = ?", UserBean.class, appVersion);
        }

    }

}

package com.haiersoft.ccli.wms.dao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BaseRemindTask;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRemindDAO extends HibernateDao<BaseRemindTask, Integer> {

    public Page<BaseRemindTask> page(Page<BaseRemindTask> page, BaseRemindTask remindTask) {

        StringBuffer sql = new StringBuffer("SELECT brt.ID as id ,brt.CONTENT AS content,brt.STATE AS state,brt.CREATE_TIME AS createTime FROM BASE_REMIND_TASK brt WHERE brt.USER_NAME = :userName");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", remindTask.getUserName());

        if (remindTask.getState() >= 0) {
            sql.append(" AND brt.state = :state ");
            params.put("state", remindTask.getState());
        }

        if (null != remindTask.getStartTime()) {
//            sql.append(" AND brt.create_time >= to_date(:startTime,'yyyy-mm-dd') ");
            sql.append(" AND brt.create_time >= :startTime ");
            params.put("startTime",remindTask.getStartTime());
        }

        if (null != remindTask.getEndTime()) {
//            sql.append(" AND brt.create_time <= to_date(:endTime,'yyyy-mm-dd') ");
            sql.append(" AND brt.create_time <= :endTime ");
            params.put("endTime",remindTask.getEndTime());
        }

        //查询对象属性转换
        Map<String, Object> paramType = new HashMap<String, Object>();
        paramType.put("id", Integer.class);
        paramType.put("content", String.class);
        paramType.put("state", Integer.class);
        paramType.put("createTime", Date.class);

        return findPageSql(page, sql.toString(), paramType, params);
    }

    /**
     * 根据用户名查找是否有未读任务提醒
     *
     * @param userName
     * @return 0有未读任务 1没有未读任务
     */
    @SuppressWarnings("unchecked")
	public String hasUnreadTask(String userName) {

        if (isNull(userName)) return "-1";

        String sql = "SELECT count(*) AS total FROM BASE_REMIND_TASK brt " +
                "WHERE brt.STATE = '0' " +
                "AND brt.USER_NAME = :userName " +
                "AND brt.TYPE = '3' " +
                "OR brt.TYPE = '4' ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        String total = list.get(0).get("TOTAL").toString();

        return Integer.parseInt(total) > 0 ? "0" : "1";

    }

    public String readMsg(String id) {
        return "";
    }
    

}

package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BaseLuodiPosition;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/13.
 */
@Repository
public class LuodiPositionDAO extends HibernateDao<BaseLuodiPosition, Integer> {

    public Page<BaseLuodiPosition> pageLuodiPosition(Page<BaseLuodiPosition> page, BaseLuodiPosition entity) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  blp.ID             AS id,\n" +
                "  blp.POSITION_NAME  AS positionName,\n" +
                "  blp.POSITION_FIRST AS positionFirst,\n" +
                "  blp.POSITION_LAST  AS positionLast,\n" +
                "  blp.POSITION_MAX   AS positionMax,\n" +
                "  blp.NOW_NUM        AS nowNum,\n" +
                "  blp.STATE          AS state,\n" +
                "  blp.DESCRIPTION    AS description\n" +
                " FROM BASE_LUODI_POSITION blp\n" +
                " WHERE 1 = 1 AND blp.STATE = 0\n";

        sql += " ORDER BY positionName";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("positionName", String.class);
        paramType.put("positionFirst", String.class);
        paramType.put("positionLast", String.class);
        paramType.put("positionMax", Integer.class);
        paramType.put("nowNum", Integer.class);
        paramType.put("state", Integer.class);
        paramType.put("description", String.class);

        return findPageSql(page, sql, paramType, params);
    }

    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> listLuodiPosition(String position) {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT\n" +
                "  blp.ID AS id,\n" +
                "  blp.POSITION_NAME AS positionName\n" +
                "FROM BASE_LUODI_POSITION blp\n" +
                "WHERE blp.STATE = 0\n";
//blp.NOW_NUM < blp.POSITION_MAX AND
        if (isNotNull(position)) {
            sql += "AND blp.POSITION_NAME LIKE '%" + position + "%'\n";
        }

        sql += " ORDER BY POSITION_NAME ";

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public BaseLuodiPosition getLuodiPositionByName(String positionName) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("positionName", positionName);
        params.put("state", 0);

        List<BaseLuodiPosition> result = findBy(params);

        if (result.size() == 1)
            return result.get(0);

        return null;

    }

    @SuppressWarnings("unused")
	public void updatePositionNum(BaseLuodiPosition luodiPosition) {

        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE BASE_LUODI_POSITION blp SET blp.NOW_NUM = :nowNum WHERE blp.ID = :id";
        params.put("id", luodiPosition.getId());
        params.put("nowNum", luodiPosition.getNowNum());

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        int result = sqlQuery.executeUpdate();
    }

}

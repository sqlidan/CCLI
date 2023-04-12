package com.haiersoft.ccli.wms.dao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.wms.entity.BisLuodiInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LuodiDAO extends HibernateDao<BisLuodiInfo, Integer> {

    public Page<BisLuodiInfo> pageLuodi(Page<BisLuodiInfo> page, BisLuodiInfo entity) {

        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT\n" +
                "  bli.ID                      AS id,\n" +
                "  bli.LUODI_CODE              AS luodiCode,\n" +
                "  bli.LUODI_TIME              AS luodiTime,\n" +
                "  bli.LUODI_TYPE              AS luodiType,\n" +
                "  blp1.POSITION_NAME          AS startPosition,\n" +
                "  blp2.POSITION_NAME          AS endPosition,\n" +
                "  bli.CTN_NUM                 AS ctnNum,\n" +
                "  bli.CTN_VERSION             AS ctnVersion,\n" +
                "  bli.CTN_SIZE                AS ctnSize,\n" +
                "  bli.CLIENT_NAME             AS clientName,\n" +
                "  bli.YF_CLIENT_NAME          AS yfClientName,\n" +
                "  bli.BILL_CODE               AS billCode,\n" +
                "  bli.STATE                   AS state,\n" +
                "  bli.START_TIME              AS startTime,\n" +
                "  bli.END_TIME                AS endTime,\n" +
                "  bli.IS_ELECTRICITY          AS isElectricity,\n" +
                "  bli.IS_COMPLETE_ELECTRICITY AS isCompleteElectricity,\n" +
                "  bpb.IF_CD                   AS isCd,\n" +
                "  bpb.CD_STATE                AS cdState,\n" +
                "  bli.CREATE_USER             AS createUser,\n" +
                "  bli.CREATE_TIME             AS createTime,\n" +
                "  bli.CAR_NUM                 AS carNum\n" +
                "FROM BIS_LUODI_INFO bli LEFT JOIN BASE_LUODI_POSITION blp1 ON bli.START_POSITION = blp1.ID\n" +
                "  LEFT JOIN BASE_LUODI_POSITION blp2 ON bli.END_POSITION = blp2.ID\n" +
                "  LEFT JOIN BIS_PRESENCE_BOX bpb ON bpb.CTN_NUM = bli.CTN_NUM\n" +
                "WHERE 1 = 1";

        sql += " ORDER BY bli.ID DESC";

        Map<String, Object> paramType = new HashMap<>();
        paramType.put("id", Integer.class);
        paramType.put("luodiCode", String.class);
        paramType.put("luodiTime", Date.class);
        paramType.put("startPosition", String.class);
        paramType.put("endPosition", String.class);
        paramType.put("ctnNum", String.class);
        paramType.put("ctnVersion", String.class);
        paramType.put("ctnSize", String.class);
        paramType.put("clientName", String.class);
        paramType.put("yfClientName", String.class);
        paramType.put("billCode", String.class);
        paramType.put("state", String.class);
        paramType.put("startTime", Date.class);
        paramType.put("endTime", Date.class);
        paramType.put("isElectricity", Integer.class);
        paramType.put("isCd", String.class);
        paramType.put("cdState", String.class);
        paramType.put("createUser", String.class);
        paramType.put("createTime", Date.class);
        paramType.put("carNum", String.class);
        paramType.put("luodiType", String.class);
        paramType.put("isCompleteElectricity", String.class);

        return findPageSql(page, sql, paramType, params);
    }

    public int updatePlan(String ctnNum, Date endTime, String endPosition) {

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "UPDATE BIS_LUODI_INFO bli\n" +
                "SET bli.END_POSITION = :endPosition ,bli.END_TIME = :endTime\n" +
                "WHERE bli.CTN_NUM = :ctnNum";

        params.put("ctnNum", ctnNum);
        params.put("endPosition", endPosition);
        params.put("endTime", endTime);

        SQLQuery sqlQuery = createSQLQuery(sql, params);

        return sqlQuery.executeUpdate();

    }

    /**
     * 获取已落地状态为0的箱子
     */
    public List<BisLuodiInfo> getLuodiInfoByCtn(String ctn) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ctnNum", ctn);
        params.put("state", "1");
        return findBy(params);
    }
    
    /**
     * 获取已落地状态为0的箱子
     */
    public List<BisLuodiInfo> getLuodiInfoByCtnWithoutState(String ctn) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ctnNum", ctn);
        //params.put("state", "1");

        return findBy(params);

    }

    public BisLuodiInfo getLuodiInfoByCtn(String ctn, String taskType) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ctnNum", ctn);
        params.put("luodiType", taskType);
        params.put("state", "1");

        List<BisLuodiInfo> result = findBy(params);

        if (result.size() == 1)
            return result.get(0);

        return null;

    }

}

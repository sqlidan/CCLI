package com.haiersoft.ccli.cost.dao;

import com.haiersoft.ccli.common.entity.SqlAndParamVO;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.FrameworkUtil;
import com.haiersoft.ccli.cost.entity.Stevedore;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StevedoreDao extends HibernateDao<Stevedore, Integer> {

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findUserByName(String param) {
        StringBuffer sb = new StringBuffer("select name from stevedore where name like :name and (del_flag is null or del_flag='0') ");
        HashMap<String, Object> parme = new HashMap<String, Object>();
        parme.put("name", "%" + param + "%");
        SQLQuery sqlQuery = createSQLQuery(sb.toString(), parme);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }


    public Page<Stevedore> pageStevedore(Page<Stevedore> page,List<PropertyFilter> filters) {

        //Map<String, Object> params = new HashMap<>();

        String sql ="SELECT \n" +
                "t1.ID as id,\n" +
                "t1.COMPANY_ID as companyId,\n" +
                "t2.CLIENT_NAME as companyName,\n" +
                "t1.NAME as name,\n" +
                "t1.GENDER as gender,\n" +
                "t1.PHONE as phone,\n" +
                "t1.CREATE_DATE as createDate,\n" +
                "t1.STATE as state,\n" +
                "t1.DEL_FLAG AS delFlag \n" +
                "FROM STEVEDORE t1 \n" +
                "LEFT JOIN BASE_CLIENT_INFO t2\n" +
                "on t1.COMPANY_ID=t2.IDS\n";

        SqlAndParamVO sqlAndParamVO=FrameworkUtil.filtersToParamsAndSQL(false,filters);
        sql+=sqlAndParamVO.strSql;

//        for(int i=0;i<filters.size();i++){
//            PropertyFilter filter=filters.get(i);
//            String propertyName=filter.getPropertyName();
//            PropertyFilter.MatchType matchType=filter.getMatchType();
//            Object value=filter.getMatchValue();
//
//            if(matchType==PropertyFilter.MatchType.LIKE)
//            {
//                sql+=" AND "+propertyName+" LIKE :"+propertyName;
//                params.put(propertyName,"%"+value+"%");
//            }
//            else if(matchType==PropertyFilter.MatchType.GT)
//            {
//                sql+=" AND  CREATE_DATE > :createDateGt";
//                params.put("createDateGt",value);
//            }
//            else if(matchType==PropertyFilter.MatchType.LT)
//            {
//                sql+=" AND CREATE_DATE < :createDateLt";
//                params.put("createDateLt",value);
//            }
//        }
//        Map<String, Object> paramType = new HashMap<>();
//        paramType.put("id", Integer.class);
//        paramType.put("companyId", Integer.class);
//        paramType.put("companyName", String.class);
//        paramType.put("name", String.class);
//        paramType.put("gender", Integer.class);
//        paramType.put("phone", String.class);
//        paramType.put("createDate", Date.class);
//        paramType.put("state", String.class);
//        paramType.put("delFlag", String.class);
        Map<String, Object> paramType=FrameworkUtil.getPropertiesTypeMap(Stevedore.class);
        return findPageSql(page, sql, paramType, sqlAndParamVO.params);
    }


}
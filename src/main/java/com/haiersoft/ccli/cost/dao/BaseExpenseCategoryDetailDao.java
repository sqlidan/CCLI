package com.haiersoft.ccli.cost.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.cost.entity.BaseExpenseCategoryDetail;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class BaseExpenseCategoryDetailDao extends HibernateDao<BaseExpenseCategoryDetail, Integer> {


    public  Map<String,Object> getCodeByFeeCode(String feecode) {
        String sql = "SELECT DETAIL_CODE,DETAIL_CODE_NAME FROM BASE_EXPENSE_CATEGORY_DETAIL WHERE FEE_ID =:feecode";
        Map<String,Object> map = new HashMap();
        Map<String,Object> map1 = new HashMap<>();
        map.put("feecode",feecode);
        List<Map<String, Object>> sqlOrSelect = findSqlOrSelect(sql, map);
        if(!StringUtils.isEmpty(sqlOrSelect)){
            for (Map<String, Object> stringObjectMap : sqlOrSelect) {
                map1 = stringObjectMap;
            }
            return map1;
        }
        return null;
    }
}

package com.haiersoft.ccli.common.utils;

import com.haiersoft.ccli.common.entity.SqlAndParamVO;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrameworkUtil {
    private static Logger logger= LoggerFactory.getLogger(FrameworkUtil.class);

    public static Map<String,Object> getPropertiesTypeMap(Class<?> clazz){
        Map<String, Object> paramTypeMap = new HashMap<>();
        Field[] field = clazz.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            Class<?> type = field[i].getType();
            String fieldName=field[i].getName();
            //logger.error(type.getName() + " " + field[i].getName() + ";");
            if(!fieldName.equals("serialVersionUID")){
                //这个地方暂时先这样，正规的方法是获取getter的列表
                //然后从getter里面提取属性列表
                paramTypeMap.put(fieldName,type);
            }
        }
        return paramTypeMap;
    }

    public static SqlAndParamVO filtersToParamsAndSQL(boolean alreadyHasWhere, List<PropertyFilter> filters){
        StringBuffer sbSQL=new StringBuffer();
        Map<String, Object> params = new HashMap<>();

        if((!alreadyHasWhere)&&(filters.size()!=0)){
            sbSQL.append(" WHERE ");
        }
        for (int i = 0; i < filters.size(); i++) {
            PropertyFilter filter = filters.get(i);
            String propertyName = filter.getPropertyName();
            PropertyFilter.MatchType matchType = filter.getMatchType();
            Object value = filter.getMatchValue();

            String strAND;
            if(alreadyHasWhere){
                strAND=" AND ";
            }else{
                if(0==i){
                    strAND=" ";
                }else{
                    strAND=" AND ";
                }
            }

            if (matchType == PropertyFilter.MatchType.LIKE) {
                sbSQL.append(strAND + propertyName + " LIKE :" + propertyName);
                params.put(propertyName, "%" + value + "%");
            } else if (matchType == PropertyFilter.MatchType.GT) {
                String thePropertyName=propertyName+"GT";
                sbSQL.append(strAND + propertyName + " > :" + thePropertyName);
                //sql += " AND  t1.CREATED_TIME > :createDateGt";
                params.put(thePropertyName, value);
            }else if (matchType == PropertyFilter.MatchType.GE) {
                String thePropertyName=propertyName+"GE";
                sbSQL.append(strAND + propertyName + " >= :" + thePropertyName);
                //sql += " AND  t1.CREATED_TIME > :createDateGt";
                params.put(thePropertyName, value);
            } else if (matchType == PropertyFilter.MatchType.LT) {
                String thePropertyName=propertyName+"LT";
                sbSQL.append(strAND + propertyName + " < :" + thePropertyName);
                //sql += " AND t1.CREATED_TIME < :createDateLt";
                params.put(thePropertyName, value);
            }else if (matchType == PropertyFilter.MatchType.LE) {
                String thePropertyName=propertyName+"LE";
                sbSQL.append(strAND + propertyName + " <= :" + thePropertyName);
                //sql += " AND t1.CREATED_TIME < :createDateLt";
                params.put(thePropertyName, value);
            }else if (matchType == PropertyFilter.MatchType.EQ) {
                String thePropertyName=propertyName+"EQ";
                sbSQL.append(strAND + propertyName + " = :" + thePropertyName);
                //sql += " AND t1.CREATED_TIME < :createDateLt";
                params.put(thePropertyName, value);
            }
        }
        SqlAndParamVO sqlAndParamVO=new SqlAndParamVO();
        sqlAndParamVO.strSql=sbSQL.toString();
        sqlAndParamVO.params =params;
        return sqlAndParamVO;
    }

}

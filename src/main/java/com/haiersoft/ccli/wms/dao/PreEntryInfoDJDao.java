package com.haiersoft.ccli.wms.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Repository
public class PreEntryInfoDJDao extends HibernateDao<BisPreEntryInfoDJ, Integer> {
    public int saveDJ(String forId,String userName,String fileName,String fileSize,byte[] fileContent,String remark){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT SEQ_PREENTRY_INFO_DJ.nextval AS NUM FROM DUAL ");
        int maxId = (Integer)(this.getSession().createSQLQuery(sb.toString()).addScalar("NUM", StandardBasicTypes.INTEGER)).uniqueResult();
        params.put("id", maxId);
        params.put("forId", forId);
        params.put("createBy", userName);
        params.put("createTime", new Date());
        params.put("filename", fileName);
        params.put("filesize", fileSize);
        params.put("filecontent", fileContent);
        params.put("remark", remark);
        String sql = "insert into BIS_PREENTRY_INFO_DJ (ID,FOR_ID,CREATE_BY,CREATE_TIME,FILENAME,FILESIZE,FILECONTENT,REMARK) " +
                " values (:id,:forId,:createBy,:createTime,:filename,:filesize,:filecontent,:remark)";
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        int result = sqlQuery.executeUpdate();
        return result;
    }
}

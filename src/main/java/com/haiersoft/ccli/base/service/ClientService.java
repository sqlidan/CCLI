package com.haiersoft.ccli.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haiersoft.ccli.base.dao.ClientDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;

@Service
public class ClientService extends BaseService<BaseClientInfo, Integer> {
    @Autowired
    private ClientDao clientDao;

    @Override
    public HibernateDao<BaseClientInfo, Integer> getEntityDao() {
        return clientDao;
    }

    /**
     * 按唯一对象字段查询客户
     *
     * @param demoleField 客户对象字段
     * @param queryStr    查询内容
     * @return 客户对象
     */
    public List<Object> getClient(String demoleField, String queryStr) {
        List<Object> getList = null;
        if (!"".equals(demoleField) && !"".equals(queryStr)) {
            StringBuffer sbHQL = new StringBuffer("from BaseClientInfo where delFlag=0 and ");
            sbHQL.append(demoleField).append("=:val");
            Map<String, Object> parme = new HashMap<String, Object>();
            parme.put("val", queryStr);
            getList = clientDao.find(sbHQL.toString(), parme);
        }
        return getList;
    }

    /*
     * 通过id获取集合（不确定此ID是否存在）
     */
    public List<BaseClientInfo> judgeById(Integer ids) {
        return clientDao.findBy("ids", ids);
    }


    //获取client(无视大小写)
    public List<Map<String, Object>> findClient(String param, String clientSort) {
        return clientDao.findClient(param, clientSort);
    }

    public List<BaseClientInfo> findBy(Map<String, Object> params) {
        return clientDao.findBy(params);
    }

    /**
     * 批量查询客户 ID ()
     *
     * @param demoleField 客户对象字段
     * @param queryStr    查询内容
     * @return 客户对象
     */
    public List<Integer> getClientIds(String demoleField, List<String> queryList) {
        List<Integer> getList = null;
        if (!"".equals(demoleField) && !(null == queryList)) {
            // StringBuffer sbHQL=new StringBuffer("select ids from BaseClientInfo where
            // delFlag=0 and client_code in ('ZNP','SOU') ");
            StringBuffer sbHQL = new StringBuffer("select ids from BaseClientInfo where delFlag=0 and ");
            String queryStr = "";
            for (int i = 0; i < queryList.size() - 1; i++) {
                queryStr = queryStr + "'" + queryList.get(i) + "',";
            }
            queryStr = queryStr + "'" + queryList.get(queryList.size() - 1) + "'";

            sbHQL.append(demoleField).append(" in (" + queryStr + ")");
            getList = clientDao.find(sbHQL.toString());
        }
        return getList;
    }
}

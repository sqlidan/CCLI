package com.haiersoft.ccli.api.service;

import com.haiersoft.ccli.api.dao.CustomerQueryConfirmDao;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryConfirm;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerQueryConfirmService extends BaseService<ApiCustomerQueryConfirm, String> {
    @Autowired
    private CustomerQueryConfirmDao customerQueryConfirmDao;

    @Override
    public HibernateDao<ApiCustomerQueryConfirm, String> getEntityDao() {
        return customerQueryConfirmDao;
    }

    public List<String> getClientIdList(String recordId) {

        return customerQueryConfirmDao.findClientIdList(recordId);
    }

    @Transactional(readOnly=false)
    public void updateCustomerInfo(String recordId, List<Integer> newCustomerList) {
        String hql="delete ApiCustomerQueryConfirm a where a.applyId=?0";
        customerQueryConfirmDao.batchExecute(hql, recordId);
        for(Integer clientId:newCustomerList){
            ApiCustomerQueryConfirm con= new ApiCustomerQueryConfirm();
            con.setApplyId(recordId);
            con.setClientId(clientId);
            customerQueryConfirmDao.insert(con);
        }

    }

    public List<Integer> checkCLients(List<Integer> clientIds) {
        StringBuffer queryStr =  new StringBuffer();
        for (int i = 0;i<clientIds.size()-1;i++) {
            queryStr.append(clientIds.get(i)).append(",");
        }
        queryStr.append(clientIds.get(clientIds.size()-1));

        return customerQueryConfirmDao.checkCLients(queryStr.toString());
    }
}

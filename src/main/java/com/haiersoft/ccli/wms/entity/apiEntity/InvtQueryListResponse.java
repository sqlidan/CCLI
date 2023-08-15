package com.haiersoft.ccli.wms.entity.apiEntity;

import java.util.List;

/**
 * @Author sunzhijie
 *
 */
//@Data
//@ApiModel(value = "InvtQueryListResponse")
public class InvtQueryListResponse implements java.io.Serializable{
    private List<InvtQueryListResponseResultList> ResultList;

    public List<InvtQueryListResponseResultList> getResultList() {
        return ResultList;
    }

    public void setResultList(List<InvtQueryListResponseResultList> resultList) {
        ResultList = resultList;
    }
}

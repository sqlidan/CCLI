package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.wms.dao.LuodiDAO;
import com.haiersoft.ccli.wms.entity.BisLuodiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LuodiService extends BaseService<BisLuodiInfo, Integer> {

    @Autowired
    private LuodiDAO luodiDAO;

    @Override
    public HibernateDao<BisLuodiInfo, Integer> getEntityDao() {
        return luodiDAO;
    }

    public Page<BisLuodiInfo> pageLuodi(Page<BisLuodiInfo> page, BisLuodiInfo entity) {
        return luodiDAO.pageLuodi(page, entity);
    }

    public String getSchemeIdToString() {
        int num = luodiDAO.getSequenceId("SEQ_SCHEME");
        return "LD" + StringUtils.lpadInt(8, num);
    }

    public int updatePlan(String ctnNum, Date endTime, String endPosition) {
        return luodiDAO.updatePlan(ctnNum, endTime, endPosition);
    }

    public List<BisLuodiInfo> getLuodiInfoByCtn(String ctn) {
        return luodiDAO.getLuodiInfoByCtn(ctn);
    }
    
    public List<BisLuodiInfo> getLuodiInfoByCtnWithoutState(String ctn) {
        return luodiDAO.getLuodiInfoByCtnWithoutState(ctn);
    }

    public BisLuodiInfo getLuodiInfoByCtn(String ctn, String taskType) {
        return luodiDAO.getLuodiInfoByCtn(ctn, taskType);
    }

}

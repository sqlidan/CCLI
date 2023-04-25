package com.haiersoft.ccli.wms.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.InOutDao;
import com.haiersoft.ccli.wms.entity.BisInOut;

/**
 * @ClassName: InOutService
 * @Description: 进出场记录Service
 */
@Service
public class InOutService extends BaseService<BisInOut, Integer> {

    @Autowired
    private InOutDao inOutDao;

    @Override
    public HibernateDao<BisInOut, Integer> getEntityDao() {
        return inOutDao;
    }

    public int getNo() {
        return inOutDao.getSequenceId("SEQ_GATE_HISTORY");
    }

    public BisInOut findByCtnNum(String ctnNum) {
        return find("ctnNum", ctnNum);
    }
    
    public int deleteList(String carNum){
    	return inOutDao.getSession().createSQLQuery(" delete from BIS_IN_OUT where CAR_NUM='"+carNum+"'").executeUpdate();
    }

}

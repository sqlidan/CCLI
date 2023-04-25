package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.GateDAO;
import com.haiersoft.ccli.wms.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/20.
 */
@Service
public class GateService extends BaseService<GateEntity, String> {

    @Autowired
    private GateDAO gateDAO;

    //@Autowired
    //private GateCarService gateCarService;

    //@Autowired
    //private InOutService inOutService;

    //@Autowired
    //private PresenceBoxService presenceBoxService;

    @Override
    public HibernateDao<GateEntity, String> getEntityDao() {
        return gateDAO;
    }

    public Page<GateEntity> page(Page<GateEntity> page, GateEntity entity) {
        return gateDAO.page(page, entity);
    }

//    public String mergeForm(GateEntity gateEntity) {
//
//        String ctnNum = gateEntity.getCtnNum();
//
//        // 车辆信息
//        BisGateCar gateCar = new BisGateCar();
//        gateCar.setCtnNum(ctnNum);
//        gateCar.setCarNum(gateEntity.getCarNum());
//        gateCar.setDriverName(gateEntity.getDriverName());
//        gateCar.setState(0);
//        gateCar.setCreateDate(gateEntity.getCreateDate());
//
//        // 进出场信息
//        BisInOut inOut = new BisInOut();
//        inOut.setXh(ctnNum);
//        inOut.setXx(gateEntity.getCtnVersion());
//        inOut.setCc(gateEntity.getCtnSize());
//        inOut.setAsn(gateEntity.getAsn());
//        inOut.setTdh(gateEntity.getBillNum());
//        inOut.setCreateDate(gateEntity.getCreateDate());
//
//        // 在场箱信息
//        BisPresenceBox presenceBox = new BisPresenceBox();
//        presenceBox.setXh(ctnNum);
//        presenceBox.setXx(gateEntity.getCtnVersion());
//        presenceBox.setCc(gateEntity.getCtnSize());
//        presenceBox.setAsn(gateEntity.getAsn());
//        presenceBox.setTdh(gateEntity.getBillNum());
//        presenceBox.setZxdw(gateEntity.getClientId());
//        presenceBox.setZxdwm(gateEntity.getClientName());
//        presenceBox.setCwid(gateEntity.getPositionId());
//        presenceBox.setCw(gateEntity.getPosition());
//        presenceBox.setCreateDate(gateEntity.getCreateDate());
//
//        gateCarService.merge(gateCar);
//        inOutService.merge(inOut);
//        presenceBoxService.merge(presenceBox);
//
//        return SUCCESS;
//
//    }

    public List<Map<String, Object>> findAsnAndBillNumByCtnNum(String ctnNum) {
        return gateDAO.findAsnAndBillNumByCtnNum(ctnNum);
    }

}

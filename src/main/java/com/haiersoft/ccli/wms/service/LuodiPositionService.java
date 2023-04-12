package com.haiersoft.ccli.wms.service;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.wms.dao.LuodiPositionDAO;
import com.haiersoft.ccli.wms.entity.BaseLuodiPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by galaxy on 2017/6/13.
 */
@Service
public class LuodiPositionService extends BaseService<BaseLuodiPosition, Integer> {

    @Autowired
    private LuodiPositionDAO luodiPositionDAO;

    @Override
    public HibernateDao<BaseLuodiPosition, Integer> getEntityDao() {
        return luodiPositionDAO;
    }

    public Page<BaseLuodiPosition> pageLuodiPosition(Page<BaseLuodiPosition> page, BaseLuodiPosition entity) {
        return luodiPositionDAO.pageLuodiPosition(page, entity);
    }

    public List<Map<String, Object>> listLuodiPosition(String position) {
        return luodiPositionDAO.listLuodiPosition(position);
    }

    public BaseLuodiPosition getLuodiPositionByName(String name) {
        return luodiPositionDAO.getLuodiPositionByName(name);
    }

    public boolean addPositionNum(int id) {
        return this.addPositionNum(this.get(id));
    }

    public boolean reducePositionNum(int id) {
        return this.reducePositionNum(this.get(id));
    }

    public boolean addPositionNumByName(String positionName) {
        return this.addPositionNum(this.getLuodiPositionByName(positionName));
    }

    public boolean reducePositionNumByName(String positionName) {
        return this.reducePositionNum(this.getLuodiPositionByName(positionName));
    }

    private boolean addPositionNum(BaseLuodiPosition luodiPosition) {

        Integer nowNum = luodiPosition.getNowNum();
        Integer maxNum = luodiPosition.getPositionMax();

        if (nowNum >= maxNum) return false;

        nowNum++;

        luodiPosition.setNowNum(nowNum);

        luodiPositionDAO.updatePositionNum(luodiPosition);

//        this.update(luodiPosition);

        return true;
    }

    private boolean reducePositionNum(BaseLuodiPosition luodiPosition) {

        Integer nowNum = luodiPosition.getNowNum();

        if (nowNum <= 0) return false;

        nowNum--;

        luodiPosition.setNowNum(nowNum);

        luodiPositionDAO.updatePositionNum(luodiPosition);

//        this.update(luodiPosition);

        return true;
    }

}

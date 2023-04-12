package com.haiersoft.ccli.wms.dao;

import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.wms.entity.BisCiqDeclaration;

/**
 * 
 * @author pyl
 * @ClassName: CiqDeclarationDao
 * @Description: 入库报检单DAO
 * @date 2016年4月16日 下午3:52:06
 */
@Repository
public class CiqDeclarationDao extends HibernateDao<BisCiqDeclaration, String>{
}

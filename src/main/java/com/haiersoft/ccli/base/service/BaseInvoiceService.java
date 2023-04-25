/**
 * 
 */
package com.haiersoft.ccli.base.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.BaseInvoiceDao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoice;

/**
 * @author Administrator
 *
 */
@Service
public class BaseInvoiceService extends BaseService<BaseInvoice, Integer>{

	@Autowired
	private BaseInvoiceDao baseInvoiceDao;
	
	@Override
	public HibernateDao<BaseInvoice, Integer> getEntityDao() {
		return baseInvoiceDao;
	}

}

/**
 * 
 */
package com.haiersoft.ccli.base.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.BaseInvoiceDetailDao;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoiceDetail;
/**
 * @author Administrator
 *
 */
@Service
public class BaseInvoiceDetailService extends BaseService<BaseInvoiceDetail, Integer>{
	
	@Autowired
	private BaseInvoiceDetailDao baseInvoiceDetailDao;

	@Override
	public HibernateDao<BaseInvoiceDetail, Integer> getEntityDao() {
		return baseInvoiceDetailDao;
	}
}

/**
 * 
 */
package com.haiersoft.ccli.base.dao;
import org.springframework.stereotype.Repository;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoice;

/**
 * @author Administrator
 *
 */
@Repository
public class BaseInvoiceDao extends HibernateDao<BaseInvoice, Integer>{

}

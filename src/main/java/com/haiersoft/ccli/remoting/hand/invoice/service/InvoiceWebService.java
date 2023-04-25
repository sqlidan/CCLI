/**
 * 
 */
package com.haiersoft.ccli.remoting.hand.invoice.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jws.WebService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haiersoft.ccli.base.dao.BaseInvoiceDao;
import com.haiersoft.ccli.base.dao.BaseInvoiceDetailDao;
import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.service.BisCheckingBookService;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoice;
import com.haiersoft.ccli.remoting.hand.invoice.entity.BaseInvoiceDetail;
/**
 * 
 * @author jxk
 *
 */
@WebService
@Service
public class InvoiceWebService {
	
	@Autowired
    private BaseInvoiceDao baseInvoiceDao;
	
	@Autowired
    private BaseInvoiceDetailDao baseInvoiceDetailDao;
	
	@Autowired
    private BisCheckingBookService bisCheckingBookService;
	
	@Autowired
    private ClientService clientService;

    /**
     * 根据字符串获取返回结果处理   
     * @param json
     * @return
     */
	public String getResult(String json){
		JSONObject result=new JSONObject();
		try{
			JSONObject object = JSONObject.fromObject(json);
			String invoicedata=object.getString("invoicedata");//主表
			JSONObject invoice=JSONObject.fromObject(invoicedata);
			JSONArray srcBizDocIdArray=invoice.getJSONArray("srcBizDocId");
			BaseInvoice baseInvoice=new BaseInvoice();
			baseInvoice.setSrcBizDocId(srcBizDocIdArray.get(0).toString());
			baseInvoice.setSrcBizDocNo(srcBizDocIdArray.get(0).toString());
			baseInvoice.setCancelStatus(invoice.getString("cancelStatus"));
			baseInvoice.setInvoicepkid(invoice.getString("invoicepkid"));
			baseInvoice.setTax(invoice.getString("tax")!=null&&!"".equals(invoice.getString("tax"))?Double.valueOf(invoice.getString("tax")):0d);
			baseInvoice.setCustomName(invoice.getString("customName"));
			baseInvoice.setCreateUser(invoice.getString("createUser"));
			baseInvoice.setNoTaxAmount(invoice.getString("noTaxAmount")!=null&&!"".equals(invoice.getString("tax"))?Double.valueOf(invoice.getString("noTaxAmount")):0d);
			baseInvoice.setInvoiceCode(invoice.getString("invoiceCode"));
			baseInvoice.setOperatorUser(invoice.getString("operatorUser"));
			baseInvoice.setCheckUser(invoice.getString("checkUser"));
			baseInvoice.setRedStatus(invoice.getString("redStatus"));
			baseInvoice.setTotalAmount(invoice.getString("totalAmount")!=null&&!"".equals(invoice.getString("totalAmount"))?Double.valueOf(invoice.getString("totalAmount")):0d);
			baseInvoice.setInvoiceType(invoice.getString("invoiceType"));
			baseInvoice.setInvoiceNo(invoice.getString("invoiceNo"));
			baseInvoice.setTaxno(invoice.getString("taxno"));
			baseInvoice.setTotalTaxAmount(invoice.getString("totalTaxAmount")!=null&&!"".equals(invoice.getString("totalTaxAmount"))?Double.valueOf(invoice.getString("totalTaxAmount")):0d);
			baseInvoice.setSrcSystem(invoice.getString("srcSystem"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			baseInvoice.setInvDate(sdf.parse(invoice.getString("invDate")));
			//////////////明细表////////////////////////////////////////////
			JSONArray detailArray=invoice.getJSONArray("entry");
			for (int i = 0; i < detailArray.size(); i++) {
				JSONObject detali=JSONObject.fromObject(detailArray.get(i).toString());
				BaseInvoiceDetail invoiceDetail=new BaseInvoiceDetail();
				invoiceDetail.setCreateDate(new Date());
				invoiceDetail.setFmName(detali.getString("fmName"));
				invoiceDetail.setTax(detali.getString("tax")!=null&&!"".equals(detali.getString("tax"))?Double.valueOf(detali.getString("tax")):0d);
				invoiceDetail.setUnit(detali.getString("unit"));
				invoiceDetail.setModel(detali.getString("model"));
				invoiceDetail.setSourceSysBillID(baseInvoice.getSrcBizDocId());
				invoiceDetail.setTaxRate(detali.getString("taxRate")!=null&&!"".equals(detali.getString("taxRate"))?Double.valueOf(detali.getString("taxRate")):0d);
				invoiceDetail.setNoTaxPice(detali.getString("noTaxPice")!=null&&!"".equals(detali.getString("noTaxPice"))?Double.valueOf(detali.getString("noTaxPice")):0d);
				invoiceDetail.setNoTaxAmount(detali.getString("noTaxAmount")!=null&&!"".equals(detali.getString("noTaxAmount"))?Double.valueOf(detali.getString("noTaxAmount")):0d);
				invoiceDetail.setTaxAmount(detali.getString("taxAmount")!=null&&!"".equals(detali.getString("taxAmount"))?Double.valueOf(detali.getString("taxAmount")):0d);
				baseInvoiceDetailDao.save(invoiceDetail);
			}
			baseInvoiceDao.save(baseInvoice);
			///////////////////////同步账单里面的客户代码以及结果///////////////
			BisCheckingBook bisCheckingBook=bisCheckingBookService.get(baseInvoice.getSrcBizDocId());
			bisCheckingBook.setSrcCustCode(baseInvoice.getCustomName());
			bisCheckingBook.setSrcCustName(baseInvoice.getCustomName());
			bisCheckingBook.setJsfs("Y");
			bisCheckingBook.setResult("success");
			bisCheckingBookService.update(bisCheckingBook);
			///////////////////////同步客户表里面的客户代码以及结果/////////////
			BaseClientInfo clientInfo=clientService.get(Integer.valueOf(bisCheckingBook.getCustomID()));
			clientInfo.setSrcCustCode(baseInvoice.getCustomName());
			clientInfo.setSrcCustName(baseInvoice.getCustomName());
			clientService.update(clientInfo);
		}catch(Exception e){
			result.put("result", false);
			result.put("message",e.getMessage());
			return result.toString();
		}
		result.put("result", true);
		result.put("message","发票数据回写成功!");
		return result.toString();
	}
}

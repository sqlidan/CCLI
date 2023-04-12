package com.haiersoft.ccli.base.service;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.haiersoft.ccli.base.dao.ExpenseSchemeScaleDao;
import com.haiersoft.ccli.base.entity.BisExpenseSchemeScale;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.dao.StandingBookDao;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
/**
 * 
 * @author PYL
 * @ClassName: ExpenseSchemeScaleService
 * @Description: 费用分摊比例Service
 * @date 2016年2月28日 下午3:27:05
 */
@Service
@Transactional(readOnly = true)
public class ExpenseSchemeScaleService extends BaseService<BisExpenseSchemeScale,Integer> {

	@Autowired
	private ExpenseSchemeScaleDao expenseSchemeScaleDao;
	@Autowired
	private StandingBookDao standingBookDao;
	@Autowired
	private StandingBookService standingBookService;
	
	@Override
    public HibernateDao<BisExpenseSchemeScale, Integer> getEntityDao() {
	    return expenseSchemeScaleDao;
    }
	
	public List<BisExpenseSchemeScale> getSchemeScale(String linkId,String feeCode,String bosSign,String standingNum){
		return expenseSchemeScaleDao.getSchemeScale(linkId,feeCode,bosSign,standingNum);
	}
	
	public List<BisExpenseSchemeScale> getSchemeScale(String linkId,String feeCode,String custId){
		return expenseSchemeScaleDao.getSchemeScale(linkId, feeCode, custId);
	}
	/**
	 * 
	 * @author PYL
	 * @Description: 删除linkId的费目分摊
	 * @date 2016年2月29日 下午5:27:10 
	 * @return
	 * @throws
	 */
	public void delByLinkId(String linkId) {
		expenseSchemeScaleDao.deleteExpenseSchemeScaleInfos(linkId);
	}

	public List<BisExpenseSchemeScale> getExpense(String outLinkId) {
		return expenseSchemeScaleDao.findBy("linkId", outLinkId);
	}
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊  发货方  分页查询
	 * @date 2016年4月18日 下午4:39:28 
	 * @return
	 * @throws
	 */
	public Page<BisExpenseSchemeScale> searchToCastShare(Page<BisExpenseSchemeScale> page, BisExpenseSchemeScale scale){
		return expenseSchemeScaleDao.searchToCastShare(page, scale);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊  收货方  分页查询
	 * @date 2016年4月19日 上午11:13:52 
	 * @param page
	 * @param scale
	 * @return
	 * @throws
	 */
	public Page<BisExpenseSchemeScale> searchFromCastShare(Page<BisExpenseSchemeScale> page, BisExpenseSchemeScale scale){
		return expenseSchemeScaleDao.searchFromCastShare(page, scale);
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 费用分摊 操作
	 * @date 2016年4月19日 下午3:29:47 
	 * @param linkId
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String startCostShare(BisExpenseSchemeScale schemeScale){
		User user = UserUtil.getCurrentUser();
		Date now = new Date();
		//将获取未分摊的并且已审核的收货方的数量数据
		List<BisExpenseSchemeScale> toScales=expenseSchemeScaleDao.find(Restrictions.eq("linkId",schemeScale.getLinkId()),Restrictions.eq("shareSign","0"),Restrictions.eq("examineSign","1"),Restrictions.eq("bosSign","1"));
		if(null != toScales && toScales.size() > 0){
			for (BisExpenseSchemeScale scale : toScales){
				    //修改分摊状态
				    BisExpenseSchemeScale expenseSchemeScale = expenseSchemeScaleDao.find(scale.getId());
				    expenseSchemeScale.setShareSign("1");
				    expenseSchemeScaleDao.save(expenseSchemeScale);
					List<BisExpenseSchemeScale> list=expenseSchemeScaleDao.getSchemeScale(scale.getLinkId(),scale.getFeeCode(),("0".equals(scale.getBosSign())?"1":"0"),scale.getStandingNum());
					for (int i = 0; i < list.size(); i++) {
						BisExpenseSchemeScale ob=list.get(i);
						ob.setShareSign("1");
					    expenseSchemeScaleDao.save(ob);
					}
					//按比例
					if("1".equals(scale.getIfRatio())){
						//获得 台账数据
						BisStandingBook standingBook = standingBookDao.find(Integer.parseInt(scale.getStandingNum()));
						standingBook.setShareSign(1);
						standingBook.setFillSign(1);
						//入库费用调整台账的应收费目为负数
						BisStandingBook inStandingBook = new BisStandingBook();
						BeanUtils.copyProperties(standingBook, inStandingBook);//复制对象属性
						//添加相应 负数的费用
						inStandingBook.setStandingNum(standingBookService.getSequenceId());
						inStandingBook.setStandingCode(StringUtils.numToCode(String.valueOf(inStandingBook.getStandingNum()),new Date()));
						inStandingBook.setNum(-scale.getNum());
						inStandingBook.setReceiveAmount(-scale.getFentanAmount());
						inStandingBook.setRealAmount(-scale.getFentanAmount());
						inStandingBook.setShouldRmb(-scale.getFentanAmount());
						inStandingBook.setRealRmb(-scale.getFentanAmount());
						inStandingBook.setInputPersonId(user.getId().toString());
						inStandingBook.setInputPerson(user.getName());
						inStandingBook.setInputDate(now);
						inStandingBook.setScaleId(scale.getId());
						String inRemark = StringUtils.isNull(standingBook.getRemark()) ? "" : standingBook.getRemark();
						inStandingBook.setRemark(inRemark + "[系统自动按比例分摊,出库联系单："+ expenseSchemeScale.getLinkId() + "]");
					
						String inLink = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
						inStandingBook.setShareLink(inLink + schemeScale.getLinkId() + ",");
						standingBookDao.save(inStandingBook);
						//出库费用调整台账的应付费目
						BisStandingBook outStandingBook = new BisStandingBook();
						BeanUtils.copyProperties(standingBook, outStandingBook);//复制对象属性
						//添加 出库的 正数费用
						outStandingBook.setStandingNum(standingBookService.getSequenceId());
						outStandingBook.setStandingCode(StringUtils.numToCode(String.valueOf(outStandingBook.getStandingNum()),new Date()));
						outStandingBook.setLinkId(expenseSchemeScale.getLinkId());
						outStandingBook.setCustomsNum(scale.getCustomsId());
						outStandingBook.setCustomsName(scale.getCustomsName());
						outStandingBook.setCrkSign(2);
						outStandingBook.setIfReceive(1);
						outStandingBook.setNum(scale.getNum());
						outStandingBook.setReceiveAmount(scale.getFentanAmount());
						outStandingBook.setRealAmount(scale.getFentanAmount());
						outStandingBook.setShouldRmb(scale.getFentanAmount());
						outStandingBook.setRealRmb(scale.getFentanAmount());
						outStandingBook.setInputPersonId(user.getId().toString());
						outStandingBook.setInputPerson(user.getName());
						outStandingBook.setInputDate(now);
						outStandingBook.setScaleId(scale.getId());
						String outRemark = StringUtils.isNull(standingBook.getRemark()) ? "" : standingBook.getRemark();
						outStandingBook.setRemark(outRemark + "[系统自动按比例分摊,入库联系单："+ standingBook.getLinkId() + "]");
						
						String outLink = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
						outStandingBook.setShareLink(outLink + schemeScale.getLinkId() + ",");
						standingBookDao.save(outStandingBook);
						
						String link = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
						standingBook.setShareLink(link + schemeScale.getLinkId() + ",");
						standingBookDao.save(standingBook);
					    //不按比例
					} else if ("0".equals(scale.getIfRatio())){
						//判断承担方
						if("1".equals(scale.getBosSign())){//卖方承担(存货方)
							//获得  台账数据
							BisStandingBook standingBook = standingBookDao.find(Integer.parseInt(scale.getStandingNum()));
							standingBook.setShareSign(1);
							String remark = StringUtils.isNull(standingBook.getRemark()) ? "" : standingBook.getRemark();
							standingBook.setRemark(remark + "[系统自动分摊由卖方承担,出库联系单："+ expenseSchemeScale.getLinkId() + "]");
							String link = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
							standingBook.setShareLink(link + schemeScale.getLinkId() + ",");
							standingBookDao.save(standingBook);
						}else if("0".equals(scale.getBosSign())){//买方承担
							//获得  台账数据
							BisStandingBook standingBook = standingBookDao.find(Integer.parseInt(scale.getStandingNum()));
							standingBook.setShareSign(1);
							standingBook.setFillSign(1);
							
							BisStandingBook newStandingBook = new BisStandingBook();
							BeanUtils.copyProperties(standingBook, newStandingBook);//复制对象属性
							
							//添加  相应  是负数的费用
							newStandingBook.setStandingNum(standingBookService.getSequenceId());
							newStandingBook.setStandingCode(StringUtils.numToCode(String.valueOf(newStandingBook.getStandingNum()),new Date()));
							newStandingBook.setNum(-(newStandingBook.getNum() == null ? 0D : newStandingBook.getNum()));
							newStandingBook.setReceiveAmount(-(newStandingBook.getReceiveAmount() == null ? 0D : newStandingBook.getReceiveAmount()));
							newStandingBook.setRealAmount(-(newStandingBook.getRealAmount() == null ? 0D : newStandingBook.getRealAmount()));
							newStandingBook.setShouldRmb(-(newStandingBook.getShouldRmb() == null ? 0D : newStandingBook.getShouldRmb()));
							newStandingBook.setRealRmb(-(newStandingBook.getRealRmb() == null ? 0D : newStandingBook.getRealRmb()));
							newStandingBook.setInputPersonId(user.getId().toString());
							newStandingBook.setInputPerson(user.getName());
							newStandingBook.setInputDate(now);
							newStandingBook.setScaleId(scale.getId());
							String remark = StringUtils.isNull(schemeScale.getRemark()) ? "" : schemeScale.getRemark();
							newStandingBook.setRemark(remark + "[系统自动分摊由买方承担,出库联系单："+ expenseSchemeScale.getLinkId() + "]");
							
							String link1 = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
							newStandingBook.setShareLink(link1 + schemeScale.getLinkId() + ",");
							standingBookDao.save(newStandingBook);
							//添加   出库联系单  相应费目
							BisStandingBook outStandingBook = new BisStandingBook();
							BeanUtils.copyProperties(standingBook, outStandingBook);//复制对象属性
							outStandingBook.setStandingNum(standingBookService.getSequenceId());
							outStandingBook.setStandingCode(StringUtils.numToCode(String.valueOf(outStandingBook.getStandingNum()),new Date()));
							outStandingBook.setCustomsNum(scale.getCustomsId());
							outStandingBook.setCustomsName(scale.getCustomsName());
							outStandingBook.setLinkId(schemeScale.getLinkId());
							outStandingBook.setInputPersonId(user.getId().toString());
							outStandingBook.setInputPerson(user.getName());
							outStandingBook.setInputDate(now);
							outStandingBook.setScaleId(scale.getId());
							String outRemark = StringUtils.isNull(schemeScale.getRemark()) ? "" : schemeScale.getRemark();
							outStandingBook.setRemark(outRemark + "[系统自动分摊由买方承担,入库联系单："+ standingBook.getLinkId() + "]");
							
							String link2 = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
							outStandingBook.setShareLink(link2 + schemeScale.getLinkId() + ",");
							standingBookDao.save(outStandingBook);
							
							String link = StringUtils.isNull(standingBook.getShareLink()) ? "" : standingBook.getShareLink();
							standingBook.setShareLink(link + schemeScale.getLinkId() + ",");
							standingBookDao.save(standingBook);
					}
				}
			}
		}else{
			return "没有发现要进行分摊费用的数据！";
		}
		return "success";
	}
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 取消分担
	 * @date 2016年5月5日 上午10:07:39 
	 * @param schemeScale
	 * @throws
	 */
	@Transactional(readOnly = false)
	public String cancelCostShare(BisExpenseSchemeScale schemeScale){
		//删除对应台账，并且更新对应分摊状态
		List<BisExpenseSchemeScale> toScales=expenseSchemeScaleDao.find(Restrictions.eq("linkId",schemeScale.getLinkId()),Restrictions.eq("shareSign","1"),Restrictions.eq("examineSign","1"));
        for (int i = 0; i < toScales.size(); i++) {
        	BisExpenseSchemeScale scale=toScales.get(i);
        	scale.setShareSign("0");
        	expenseSchemeScaleDao.update(scale);
        	List<BisStandingBook> list=standingBookDao.find(Restrictions.eq("scaleId",scale.getId()));
        	for (int j = 0; j < list.size(); j++) {
        		BisStandingBook book=list.get(j);
        		standingBookDao.delete(book);
			}
		}
        return "success";
	}
}

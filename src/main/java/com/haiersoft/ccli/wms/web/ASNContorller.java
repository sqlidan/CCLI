package com.haiersoft.ccli.wms.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.haiersoft.ccli.common.utils.parameterReflect;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haiersoft.ccli.base.entity.BaseClientInfo;
import com.haiersoft.ccli.base.entity.BaseSkuBaseInfo;
import com.haiersoft.ccli.base.entity.BaseTaxRate;
import com.haiersoft.ccli.base.entity.ExpenseSchemeInfo;
import com.haiersoft.ccli.base.service.ClientService;
import com.haiersoft.ccli.base.service.ExpenseSchemeInfoService;
import com.haiersoft.ccli.base.service.SkuInfoService;
import com.haiersoft.ccli.base.service.TaxRateService;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.utils.BigDecimalUtil;
import com.haiersoft.ccli.common.utils.DateUtils;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.cost.entity.BisStandingBook;
import com.haiersoft.ccli.cost.service.AsnActionLogService;
import com.haiersoft.ccli.cost.service.StandingBookService;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.AsnAction;
import com.haiersoft.ccli.wms.entity.BisAsn;
import com.haiersoft.ccli.wms.entity.BisAsnInfo;
import com.haiersoft.ccli.wms.entity.BisEnterStock;
import com.haiersoft.ccli.wms.entity.BisEnterStockInfo;
import com.haiersoft.ccli.wms.entity.BisLoadingInfo;
import com.haiersoft.ccli.wms.entity.BisTransferStock;
import com.haiersoft.ccli.wms.entity.TrayInfo;
import com.haiersoft.ccli.wms.service.ASNInfoService;
import com.haiersoft.ccli.wms.service.ASNService;
import com.haiersoft.ccli.wms.service.AsnActionService;
import com.haiersoft.ccli.wms.service.EnterStockInfoService;
import com.haiersoft.ccli.wms.service.EnterStockService;
import com.haiersoft.ccli.wms.service.LoadingInfoService;
import com.haiersoft.ccli.wms.service.TransferService;
import com.haiersoft.ccli.wms.service.TrayInfoService;

/**
 * ASNcontroller
 *
 * @author lzg
 * @date 2016年3月1日
 */
@Controller
@RequestMapping("bis/asn")
public class ASNContorller extends BaseController {

    @Autowired
    private ASNService asnService;
    @Autowired
    private AsnActionService asnActionService;
    @Autowired
    private AsnActionLogService asnActionLogService;
    @Autowired
    private ASNInfoService asnInfoService;// ASN明细
    @Autowired
    private EnterStockService enterStockService;// 入库联系单
    @Autowired
    private EnterStockInfoService enterStockInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private TransferService transferService;// 货转联系单
    @Autowired
    private ExpenseSchemeInfoService expenseSchemeInfoService;// 费用方案明细
    @Autowired
    private TaxRateService taxRateService;// 汇率表
    @Autowired
    private StandingBookService standingBookService;// 台账表
    @Autowired
    private ClientService clientService;// 客户
    @Autowired
	private TrayInfoService trayInfoService;
    @Autowired
	private LoadingInfoService loadingInfoService;

    /* 跳转ASN列表页面 */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String menuList() {
        return "wms/asn/asnList";
    }

    // 入库联系单查看ASN跳转
    @RequestMapping(value = "list2/{linkId}", method = RequestMethod.GET)
    public String menuList2(@PathVariable("linkId") String linkId, Model model) {
        model.addAttribute("linkId", linkId);
        model.addAttribute("action", "enter");
        return "wms/asn/asnList";
    }

    /*
     * 列表页面table获取json
     */
    @RequiresPermissions("bis:asn:view")
    @RequestMapping(value = "listjson", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request) {
        Page<BisAsn> page = getPage(request);
        page.orderBy("createTime").order("desc");
        BisAsn bisAsn = new BisAsn();
        parameterReflect.reflectParameter(bisAsn, request);
        page = asnService.getAllASN(page, bisAsn);
      //  page.orderBy("createTime").order("desc");
     //   page.setOrder("desc");
        return getEasyUIData(page);
    }

    /**
     * 添加ASN跳转
     */
    @RequiresPermissions("bis:asn:add")
    @RequestMapping(value = "create/{linkId}", method = RequestMethod.GET)
    public String createForm(@PathVariable("linkId") String linkId, Model model) {
        BisAsn newObj = new BisAsn();
        if (linkId != null && !"000000".equals(linkId)) {
            // 获取联系单信息，填充ASN
            // 判断是货转还是入库 通过联系单标示入库联系单 前缀E 货转联系单 前缀T
//			if (linkId.indexOf("E") == 0) {
            if (linkId.length() > 1) {
                // 根据联系单id获取联系单对象
                BisEnterStock getBisEnterStock = enterStockService.get(linkId);
                if (getBisEnterStock != null) {
                    if (getBisEnterStock.getLinkId() != null) {
                        newObj.setBillNum(getBisEnterStock.getItemNum());// 提单号
                        newObj.setStockIn(getBisEnterStock.getStockId());// 存货方id
                        newObj.setIsBonded(getBisEnterStock.getIfBonded());//是否保税
                        model.addAttribute("ifEnter", "1");
                    }
                }
            } else {
                // 根据货转单id获取货转单对象
                BisTransferStock getTransferObj = transferService.get(linkId);
                if (getTransferObj != null && getTransferObj.getTransferId() != null) {
                    newObj.setBillNum("");// 提单号
                    newObj.setStockIn(getTransferObj.getStockInId());// 存货方id
                    newObj.setIsBonded("0");
                }
            }
            newObj.setLinkId(linkId);// 联系单id
        } else {
            model.addAttribute("ifEnter", "0");
            // newObj.setStockIn("0");
        }
        newObj.setAsn(asnService.getASNId());// 生成主键
        model.addAttribute("asn", newObj);
        model.addAttribute("action", "create");
        return "wms/asn/asnForm";
    }

    /**
     * 添加SKUList跳转
     */
    @RequestMapping(value = "skulist", method = RequestMethod.GET)
    public String seletSKUList(Model model) {
        return "wms/asn/addSKUList";
    }

    /**
     * 新建SKU跳转
     */
    @RequestMapping(value = "skucreate", method = RequestMethod.GET)
    public String openSKUFrom(Model model) {
        return "wms/asn/asnSkuForm";
    }

    /**
     * 添加ASN
     */
    @RequiresPermissions("bis:asn:add")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisAsn asn, Model model) {
        asn.setAsnState("1");// 添加新建状态
        User user=UserUtil.getCurrentUser();
        asn.setCreateUser(user.getName());
        asn.setCreateTime(new Date());
        return asnService.saveASN(asn);
    }

    /**
     * 查询联系单跳转
     *
     * @return
     */
    @RequestMapping(value = "enterlist", method = RequestMethod.GET)
    public String enterList() {
        return "wms/asn/searchList";
    }

    /**
     * 修改ASN跳转
     *
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("bis:asn:update")
    @RequestMapping(value = "update/{asnid}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("asnid") String asnid, Model model) {
        BisAsn getObj = asnService.get(asnid);
        if (getObj != null && 1 < Integer.valueOf(getObj.getAsnState())) {
            getObj.setIsEdite(1);
        }
        model.addAttribute("asn", getObj);
        model.addAttribute("action", "update");
        return "wms/asn/asnForm";
    }

    /**
     * 修改ASN
     *
     * @param BisAsn
     * @param model
     * @return
     */
    @RequiresPermissions("bis:asn:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@Valid @ModelAttribute @RequestBody BisAsn asn,
                         Model model) {
        return asnService.updateASN(asn);
    }

    /**
     * 删除ASN
     *
     * @param id
     * @return
     */
    @RequiresPermissions("bis:asn:delete")
    @RequestMapping(value = "delete/{asnid}")
    @ResponseBody
    public String delete(@PathVariable("asnid") String asnid) {
        String retStr = "success";
        BisAsn getObj = asnService.get(asnid);
        // 在途过程中可以删除
        if (getObj != null && "1".equals(getObj.getAsnState())) {
            retStr = asnService.deleteASN(asnid);
        } else {
            retStr = "state";
        }
        return retStr;
    }

    /**
     * 完结ASN
     *
     * @param user
     * @param model
     * @return
     */
    @SuppressWarnings("unused")
	@RequiresPermissions("bis:asn:endd")
    @RequestMapping(value = "endd/{asnid}", method = RequestMethod.POST)
    @ResponseBody
    public String endd(@PathVariable("asnid") String asnid) {
        String retStr = "success";
        BisAsn getObj = asnService.get(asnid);
        BisEnterStock enterStock = enterStockService.get(getObj.getLinkId());
        ExpenseSchemeInfo crExpense = null;
        ExpenseSchemeInfo fjExpense = null;
        List<Map<String, Object>> numList = new ArrayList<Map<String, Object>>();
        // 当入库类型为正常时，计算出入库费
        if (getObj.getIfSecondEnter().equals("1") || getObj.getIfSecondEnter().equals("2")) {
            Map<String, Object> cr = new HashMap<String, Object>();
            String crFeeId = enterStock.getFeeId();
            cr.put("schemeNum", crFeeId);
            cr.put("feeType", "3");
            List<ExpenseSchemeInfo> crList = expenseSchemeInfoService.getFeeByName(cr);
            if (crList.isEmpty()) {
                return "nofee";
            } else {
                crExpense = crList.get(0);
            }
        }//end if出入库

        //当入库类型为分拣或入库联系单选了分拣时，计算分拣费
        if (getObj.getIfSecondEnter().equals("3") || enterStock.getIfSorting().equals("1")) {
            Map<String, Object> fj = new HashMap<String, Object>();
            String fjFeeId = enterStock.getFeeId();
            fj.put("schemeNum", fjFeeId);
            fj.put("feeType", "4");
            List<ExpenseSchemeInfo> fjList = expenseSchemeInfoService.getFeeByName(fj);
            if (fjList.isEmpty()) {
                return "nofjfee";
            } else {
                int size = fjList.size();
                Double max = 0.00;
                Double min = 0.00;
                ExpenseSchemeInfo exInfo = null;
                //获取有几个SKU
                Map<String, Object> asnparam = new HashMap<String, Object>();
                asnparam.put("asn", asnid);
                numList = asnService.fjFee(asnparam);
                int num = numList.size();
                for (int i = 0; i < size; i++) {
                    max = fjList.get(i).getMaxPrice();
                    min = fjList.get(i).getMinPrice();
                    if (null != max && !"".equals(max) && null != min && !"".equals(min)) {
                        if (num >= min && num < max) {
                            exInfo = fjList.get(i);
                            fjExpense = fjList.get(i);
                            break;
                        }
                    }
                }
                if (exInfo == null) {
                    return "nofjok";
                }
            }
        }//end if分拣

        if (crExpense != null) {
            crFee(getObj, crExpense);
        }

        if (fjExpense != null) {
            String glz = fjFee(getObj, fjExpense, numList);
            if (!glz.equals("success")) {
                return glz;
            }
        }

        if (getObj != null) {
            if ("3".equals(getObj.getAsnState())) {
                getObj.setAsnState("4");
                asnService.save(getObj);
            } else {
                retStr = "state";
            }
        } else {
            retStr = "error";
        }
        return retStr;
    }

    /**
     * @param getObj
     * @param crExpense
     * @return
     * @Description 点击完结时，计算出入库费
     * @author PYL
     */
    @ResponseBody
    private void crFee(BisAsn getObj, ExpenseSchemeInfo exInfo) {
        User user = UserUtil.getCurrentUser();
        String asn = getObj.getAsn();
        
        //ASN 入库类型为 正常 的计算出入库费 20170921
        if(!"1".equals(getObj.getIfSecondEnter())){
        	return ;
        }
        
        String linkId = getObj.getLinkId();
        Date enterTime = getObj.getInboundTime();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("asn", asn);
        List<Map<String, Object>> numList = asnService.crFee(params);
        BigDecimal pieceB = (BigDecimal) numList.get(0).get("PIECE");
        BigDecimal netWeightB = (BigDecimal) numList.get(0).get("NETWEIGHT");
        BigDecimal grossWeightB = (BigDecimal) numList.get(0).get("GROSSWEIGHT");
        Integer piece = pieceB.intValue();
        Double netWeight = netWeightB.doubleValue();
        Double grossWeight = grossWeightB.doubleValue();
        Double price = 0.00;
        Double unit = exInfo.getUnit();
        Double jsNum = 0.00;
        switch (exInfo.getBilling()) {
            case "1":
                price = BigDecimalUtil.mul(unit, piece);
                jsNum = piece.doubleValue();
                break;
            case "2":
                price = BigDecimalUtil.mul(unit, grossWeight);
                jsNum = grossWeight / 1000;
                break;
            case "3":
                price = BigDecimalUtil.mul(unit, netWeight);
                jsNum = netWeight / 1000;
                break;
        }
        // 将费用加入台账表
        BisStandingBook standingBook = new BisStandingBook();
        standingBook.setStandingNum(standingBookService.getSequenceId());
        standingBook.setCustomsNum(getObj.getStockIn());
        standingBook.setCustomsName(getObj.getStockName());
        standingBook.setBillNum(getObj.getBillNum());
        standingBook.setAsn(asn);
        standingBook.setLinkId(linkId);
        standingBook.setFeePlan(exInfo.getSchemeNum());
        standingBook.setFeeCode(exInfo.getFeeCode());
        standingBook.setFeeName(exInfo.getFeeName());
        standingBook.setCrkSign(1);
        standingBook.setStorageDtae(enterTime);
        BaseClientInfo getClient = clientService.get(Integer.valueOf(getObj.getStockIn()));
        if (null != getClient) {
            if (null != getClient.getCheckDay()) {
                standingBook.setBillDate(DateUtils.ifBillDay(enterTime, getClient.getCheckDay()));
            }
        }
        standingBook.setIfReceive(1);
        standingBook.setNum(jsNum);
        standingBook.setPrice(unit);
        standingBook.setReceiveAmount(price / 1000);
        standingBook.setChargeDate(new Date());
        standingBook.setCostDate(new Date());
        standingBook.setInputPerson(user.getName());
        standingBook.setInputDate(new Date());
        standingBook.setFillSign(0);
        standingBook.setCurrency(exInfo.getCurrency());
        BaseTaxRate taxRate = taxRateService.getTaxByC(exInfo.getCurrency());
        standingBook.setExchangeRate(taxRate.getExchangeRate());
        standingBook.setExamineSign(0);
        standingBook.setBisType("3");
        standingBook.setShouldRmb(taxRate.getExchangeRate() * price / 1000);
        standingBook.setReconcileSign(0);
        standingBook.setSettleSign(0);
        standingBook.setSplitSign(0);
        standingBook.setRemark("自动生成asn的出入库费");
        standingBook.setContactType(1);
        standingBook.setBoxSign(0);
        standingBook.setShareSign(0);
        standingBook.setPaySign("0");
        standingBook.setChargeSign("0");
        standingBook.setRealAmount(0.00);
        standingBook.setRealRmb(0.00);
        standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
        standingBookService.save(standingBook);
    }

    /**
     * @param getObj
     * @param numList
     * @param crExpense
     * @return
     * @Description 点击完结时，计算分拣费
     * @author PYL
     */
    @ResponseBody
    private String fjFee(BisAsn getObj, ExpenseSchemeInfo exInfo, List<Map<String, Object>> numList) {
        User user = UserUtil.getCurrentUser();
        String asn = getObj.getAsn();
        String linkId = getObj.getLinkId();
        Date enterTime = getObj.getInboundTime();
        int num = numList.size();
        Integer piece = 0;
        Double netWeight = 0.00;
        Double grossWeight = 0.00;
        for (int i = 0; i < num; i++) {
            piece += ((BigDecimal) numList.get(i).get("PIECE")).intValue();
            netWeight += ((BigDecimal) numList.get(i).get("NETWEIGHT")).doubleValue();
            grossWeight += ((BigDecimal) numList.get(i).get("GROSSWEIGHT")).doubleValue();
        }
        Double price = 0.00;
        Double unit = exInfo.getUnit();
        Double jsNum = 0.00;
        switch (exInfo.getBilling()) {
            case "1":
                price = unit * piece;
                jsNum = piece.doubleValue();
                break;
            case "2":
                price = unit * grossWeight;
                jsNum = grossWeight / 1000;
                break;
            case "3":
                price = unit * netWeight;
                jsNum = netWeight / 1000;
                break;
        }
        // 将费用加入台账表
        BisStandingBook standingBook = new BisStandingBook();
        standingBook.setStandingNum(standingBookService.getSequenceId());
        standingBook.setCustomsNum(getObj.getStockIn());
        standingBook.setCustomsName(getObj.getStockName());
        standingBook.setBillNum(getObj.getBillNum());
        standingBook.setAsn(asn);
        standingBook.setLinkId(linkId);
        standingBook.setFeePlan(exInfo.getSchemeNum());
        standingBook.setFeeCode(exInfo.getFeeCode());
        standingBook.setFeeName(exInfo.getFeeName());
        standingBook.setCrkSign(1);
        standingBook.setStorageDtae(enterTime);
        BaseClientInfo getClient = clientService.get(Integer.valueOf(getObj.getStockIn()));
        if (null != getClient) {
            if (null != getClient.getCheckDay()) {
                standingBook.setBillDate(DateUtils.ifBillDay(enterTime, getClient.getCheckDay()));
            }
        }
        standingBook.setIfReceive(1);
        standingBook.setNum(jsNum);
        standingBook.setPrice(unit);
        standingBook.setChargeDate(new Date());
        standingBook.setCostDate(new Date());
        standingBook.setInputPerson(user.getName());
        standingBook.setInputDate(new Date());
        standingBook.setFillSign(0);
        standingBook.setCurrency(exInfo.getCurrency());
        BaseTaxRate taxRate = taxRateService.getTaxByC(exInfo.getCurrency());
        standingBook.setExchangeRate(taxRate.getExchangeRate());
        standingBook.setExamineSign(0);
        standingBook.setBisType("4");
        if (exInfo.getBilling().equals("1")) {
            standingBook.setReceiveAmount(price);
            standingBook.setShouldRmb(taxRate.getExchangeRate() * price);
        } else {
            standingBook.setReceiveAmount(price / 1000);
            standingBook.setShouldRmb(taxRate.getExchangeRate() * price / 1000);
        }
        standingBook.setReconcileSign(0);
        standingBook.setSettleSign(0);
        standingBook.setSplitSign(0);
        standingBook.setRemark("自动生成asn的分拣费");
        standingBook.setContactType(1);
        standingBook.setBoxSign(0);
        standingBook.setShareSign(0);
        standingBook.setPaySign("0");
        standingBook.setChargeSign("0");
        standingBook.setRealAmount(0.00);
        standingBook.setRealRmb(0.00);
        standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
        standingBookService.save(standingBook);
        return "success";
    }

    /**
     * 取消完结ASN
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("bis:asn:unendd")
    @RequestMapping(value = "unendd/{asnid}", method = RequestMethod.POST)
    @ResponseBody
    public String unendd(@PathVariable("asnid") String asnid) {
        String retStr = "success";
        BisAsn getObj = asnService.get(asnid);
        if (getObj != null) {
            if ("4".equals(getObj.getAsnState())) {
                getObj.setAsnState("3");
                asnService.save(getObj);
                if (getObj.getIfSecondEnter().equals("1")
                        || getObj.getIfSecondEnter().equals("2")) {
                    crFeeCancle(asnid);
                }
                BisEnterStock enterStock = enterStockService.get(getObj
                        .getLinkId());
                if (getObj.getIfSecondEnter().equals("3")
                        || enterStock.getIfSorting().equals("1")) {
                    fjFeeCancle(asnid);
                }
            } else {
                retStr = "state";
            }
        } else {
            retStr = "error";
        }
        return retStr;
    }

    /**
     * @param getObj
     * @return
     * @Description 点击取消完结时，生成负的出入库费
     * @author PYL
     */
    private void crFeeCancle(String asnid) {
        User user = UserUtil.getCurrentUser();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("asn", asnid);
        params.put("bisType", "3");
        List<BisStandingBook> standingList = standingBookService.getFee(params);
        if (!standingList.isEmpty()) {
            BisStandingBook standing = new BisStandingBook();
            standing.setStandingNum(0);
            int size = standingList.size();
            for (int i = 0; i < size; i++) {
                if (standingList.get(i).getStandingNum() > standing
                        .getStandingNum()) {
                    standing = standingList.get(i);
                }
            }
            BisStandingBook standingBook = new BisStandingBook();
            BeanUtils.copyProperties(standing, standingBook);
            standingBook.setStandingNum(standingBookService.getSequenceId());
            standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
            standingBook.setInputPerson(user.getName());
            standingBook.setInputDate(new Date());
            standingBook.setChargeDate(new Date());
            standingBook.setCostDate(new Date());
            standingBook.setRealAmount(-standingBook.getRealAmount());
            standingBook.setRealRmb(-standingBook.getRealRmb());
            standingBook.setShouldRmb(-standingBook.getShouldRmb());
            standingBook.setReceiveAmount(-standingBook.getReceiveAmount());
            standingBook.setPrice(-standingBook.getPrice());
            standingBook.setRemark("自动生成撤销asn出入库费的负费用");
            standingBookService.save(standingBook);
        }
    }

    /**
     * @param getObj
     * @return
     * @Description 点击取消完结时，生成负的分拣费
     * @author PYL
     */
    private void fjFeeCancle(String asnid) {
        User user = UserUtil.getCurrentUser();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("asn", asnid);
        params.put("bisType", "4");
        List<BisStandingBook> standingList = standingBookService.getFee(params);
        if (!standingList.isEmpty()) {
            BisStandingBook standing = new BisStandingBook();
            standing.setStandingNum(0);
            int size = standingList.size();
            for (int i = 0; i < size; i++) {
                if (standingList.get(i).getStandingNum() > standing
                        .getStandingNum()) {
                    standing = standingList.get(i);
                }
            }
            BisStandingBook standingBook = new BisStandingBook();
            BeanUtils.copyProperties(standing, standingBook);
            standingBook.setStandingNum(standingBookService.getSequenceId());
            standingBook.setStandingCode(StringUtils.numToCode(String.valueOf(standingBook.getStandingNum()), new Date()));
            standingBook.setInputPerson(user.getName());
            standingBook.setInputDate(new Date());
            standingBook.setChargeDate(new Date());
            standingBook.setCostDate(new Date());
            standingBook.setRealAmount(-standingBook.getRealAmount());
            standingBook.setRealRmb(-standingBook.getRealRmb());
            standingBook.setShouldRmb(-standingBook.getShouldRmb());
            standingBook.setReceiveAmount(-standingBook.getReceiveAmount());
            standingBook.setPrice(-standingBook.getPrice());
            standingBook.setRemark("自动生成撤销asn分拣费的负费用");
            standingBookService.save(standingBook);
        }
    }

    // asn区间表页面
    @RequestMapping(value = "actionlist", method = RequestMethod.GET)
    public String actionlist(HttpServletRequest request) {
        return "base/asnAction";
    }

    // asn区间表查询
    @RequestMapping(value = "asnaction", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> asnaction(HttpServletRequest request) {
        Page<AsnAction> page = getPage(request);

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
        page = asnActionService.search(page, filters);
        return getEasyUIData(page);
    }

    /*
     * asn总览表获取实际入库数量
     */
    @RequestMapping(value = "pnumjson", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getPNum(HttpServletRequest request) {
        List<Map<String, Object>> getList = null;
        if (request.getParameter("asnIds") != null) {
            String asnIds = request.getParameter("asnIds").toString();// ASN
            // code
            // 集合
            if (!"".equals(asnIds)) {
                getList = asnService.getASNPNum(asnIds);
            }
        }
        return getList;
    }

    /**
     * 导入入库单明细
     *
     * @param asnId
     * @param linkId
     * @return
     */
    @RequestMapping(value = "importinfo", method = RequestMethod.GET)
    @ResponseBody
    public String importInfo(HttpServletRequest request, String asnId, String linkId, String ctnNum) {
        BisAsn asnObj = asnService.get(asnId);
        if (asnObj.getAsnState().equals("1")) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("linkId", linkId);
            params.put("ctnNum", ctnNum);
            List<BisEnterStockInfo> infoList = enterStockInfoService.getListByMap(params);
            int size = infoList.size();
            if (size != 0) {
                List<BisAsnInfo> asnList = asnInfoService.getASNInfoList(asnId);
                if (asnList.isEmpty()) {
                    User user = UserUtil.getCurrentUser();
                    BisAsnInfo asnInfo = null;
                    BisEnterStockInfo oldObj = null;
                    BaseSkuBaseInfo skuObj = null;
                    String skuId = "";
                    // asn区间表共通存储
                    AsnAction asnActionObj = new AsnAction();
                    asnActionObj.setAsn(asnId);
                    asnActionObj.setEnterId(linkId);
                    asnActionObj.setStatus("1");
                    asnActionObj.setClientId(asnObj.getStockIn());// 客户id
                    BisEnterStock enterStock = enterStockService.get(linkId);
                    asnActionObj.setJfClientId(enterStock.getStockOrgId());// 结算单位id
                    BaseClientInfo getClient = clientService.get(Integer.valueOf(enterStock.getStockOrgId()));
                    if (getClient != null) {
                        asnActionObj.setClientDay(getClient.getCheckDay());// 客户计费日期
                    }
                    // if(正常||重收){计费开始=入库日期}else{计费开始=入库日期+1} 入库类型
                    if (null != asnObj.getInboundTime()) {
                        if ("1".equals(asnObj.getIfSecondEnter().trim()) || "2".equals(asnObj.getIfSecondEnter().trim())) {
                            asnActionObj.setChargeStaDate(asnObj.getInboundTime());
                        } else {
                            asnActionObj.setChargeStaDate(DateUtils.addDay(asnObj.getInboundTime(), 1));
                        }
                    } else {
                        asnActionObj.setChargeStaDate(null);
                        asnActionObj.setChargeStaDate(null);
                    }
                    // 填入费用方案
                    if (enterStock != null) {
                        asnActionObj.setFeePlanId(enterStock.getFeeId());
                        asnActionObj.setBillCode(enterStock.getItemNum());
                    }
                    // 遍历明细
                    AsnAction newAsnAction = null;
                    for (int i = 0; i < size; i++) {
                        skuObj = new BaseSkuBaseInfo();
                        asnInfo = new BisAsnInfo();
                        oldObj = new BisEnterStockInfo();
                        oldObj = infoList.get(i);
                        skuId = oldObj.getSku();
                        skuObj = skuInfoService.get(skuId);
                        asnInfo.setAsnId(asnId);
                        asnInfo.setSkuId(skuId);
                        asnInfo.setRkNum(oldObj.getRkNum());
                        asnInfo.setSkuDescribe(skuObj.getProducingArea());
                        asnInfo.setCargoState(skuObj.getCargoState());
                        asnInfo.setValidityTime(skuObj.getValidityTime());
                        asnInfo.setAttribute1(skuObj.getAttribute1());
                        asnInfo.setAttribute2(skuObj.getAttribute2());
                        asnInfo.setAttribute3(skuObj.getAttribute3());
                        asnInfo.setCargoName(skuObj.getCargoName());
                        asnInfo.setCargoType(skuObj.getCargoType());
                        asnInfo.setTypeSize(skuObj.getTypeSize());
                        asnInfo.setPiece(oldObj.getPiece().doubleValue());
                        asnInfo.setIfSecondEnter(Integer.valueOf(asnObj
                                .getIfSecondEnter()));
                        asnInfo.setNetWeight(oldObj.getNetWeight());
                        asnInfo.setGrossWeight(oldObj.getGrossWeight());
                        asnInfo.setNetSingle(oldObj.getNetSingle());
                        asnInfo.setGrossSingle(oldObj.getGrossSingle());
                        asnInfo.setUnits("1");
                        asnInfo.setMscNum(oldObj.getMscNum());
                        asnInfo.setLotNum(oldObj.getLotNum());
                        asnInfo.setProNum(oldObj.getProjectNum()!=null?oldObj.getProjectNum():"");
                        asnInfo.setTypeSize(oldObj.getTypeSize()!=null?oldObj.getTypeSize():"");
                        String makeDate=oldObj.getMakeTime()!=null?oldObj.getMakeTime().toString().substring(0,10):" ";
                        asnInfo.setProTime(makeDate);
                        asnInfo.setOperateTime(new Date());
                        asnInfo.setOperator(user.getName());
                        asnInfoService.save(asnInfo);
                        // ASN区间表
                        newAsnAction = new AsnAction();
                        BeanUtils.copyProperties(asnActionObj, newAsnAction);// 复制
                        newAsnAction.setSku(skuObj.getSkuId());
                        newAsnAction.setCargoName(skuObj.getCargoName());
                        asnActionService.save(newAsnAction);
                        asnActionLogService.saveLog(newAsnAction, "1", 0, 0, "ASN导入入库联系单明细产生的新的ASN区间表记录(入库前)");
                    }
                    return "success";
                } else {
                    return "exist";
                }
                // end if size
            } else {
                return "blank";
            }
            // end if asnstate
        } else {
            return "nostate";
        }
    }

    /*
     * 判断ASN是否保存
     */
    @RequestMapping(value = "ifsave/{asnId}", method = RequestMethod.GET)
    @ResponseBody
    public String ifsave(@PathVariable("asnId") String asnId) {
        BisAsn asn = asnService.get(asnId);
        if (asn == null) {
            return "success";
        } else {
            return "false";
        }
    }

    /**
     * @param code
     * @return
     * @throws
     * @author PYL
     * @Description: 对应入库联系单中所有的明细的箱号
     * @date 2016年2月25日 下午6:57:14
     */
    @RequestMapping(value = "getAllMr/{linkId}", method = RequestMethod.GET)
    @ResponseBody
    public List<BisEnterStockInfo> getAllMr(@PathVariable("linkId") String linkId) {
        List<Map<String, Object>> mrList = enterStockInfoService.getMrList(linkId);
        List<BisEnterStockInfo> mrObj = new ArrayList<BisEnterStockInfo>();
        BisEnterStockInfo stockInfo = null;
        int size = mrList.size();
        String mr = "";
        String orderNum = "";
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                stockInfo = new BisEnterStockInfo();
                mr = (String) mrList.get(i).get("CTN_NUM");
                orderNum = (String) mrList.get(i).get("ORDER_NUM");
                stockInfo.setCtnNum(mr);
                stockInfo.setOrderNum(orderNum);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("linkId", linkId);
                params.put("ctnNum", mr);
                List<BisAsn> newList = asnService.getList(params);
                if (newList.isEmpty()) {
                    stockInfo.setIfasn("0");
                } else {
                    stockInfo.setIfasn("1");
                }
                mrObj.add(stockInfo);
            }
        }//end if
        return mrObj;
    }

    /*
     * 列表页面table获取json
     */
    @RequestMapping(value = "getinfonum/{asn}", method = RequestMethod.GET)
    @ResponseBody
    public String getinfonum(@PathVariable("asn") String asn) {
        List<Map<String, Object>> objList = asnService.getinfonum(asn);
        String a = "0";
        if (!objList.isEmpty()) {
            a = objList.get(0).get("PIECE").toString();
        }
        return a;
    }

    @RequestMapping(value = "getInfoCount", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getInfoCount(HttpServletRequest request) {
        BisAsn bisAsn = new BisAsn();
        parameterReflect.reflectParameter(bisAsn, request);
        return asnService.getPiece1Gross1NetCount(bisAsn);
    }

    @RequestMapping(value = "realPiece/{asn}/{sku}", method = RequestMethod.GET)
    @ResponseBody
    public String realPiece(@PathVariable("asn") String asn,@PathVariable("sku") String sku) {
    	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("asn", asn);
    	params.put("skuId", sku);
    	List<TrayInfo> trayObj=trayInfoService.findBySku(params);
    	Integer real=0;
    	if(!trayObj.isEmpty()){
    		for(TrayInfo obj:trayObj){
    			if(!obj.getCargoState().equals("99") && !obj.getCargoState().equals("00")){
    				real+=obj.getOriginalPiece();
    			}
    		}
    		return real.toString();
    	}else{
    		return "0";
    	}
    }
    
    /**
     * 重收关联装车单跳转
     */
    @RequestMapping(value = "loadingList", method = RequestMethod.GET)
    public String loadingList(Model model) {
        return "wms/asn/asnToLoading";
    }
    
    /**
     * 重收关联装车单页面选择
     */
    @RequestMapping(value = "getLoadingTime", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getLoadingTime(HttpServletRequest request) {
        Page<BisLoadingInfo> page = getPage(request);
        BisLoadingInfo bisLoadingInfo = new BisLoadingInfo();
        parameterReflect.reflectParameter(bisLoadingInfo, request);
        page = loadingInfoService.getLoadingToAsn(page, bisLoadingInfo);
        return getEasyUIData(page);
    }
    
    /**
     * 打印ASN
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("bis:asn:print")
    @RequestMapping(value = "print/{asnid}", method = RequestMethod.GET)
    public String print(@PathVariable("asnid") String asnid, Model model) {
        if (asnid != null && !"".equals(asnid)) {
            BisAsn getObj = asnService.get(asnid);
            BisEnterStock enterObj = enterStockService.get(getObj.getLinkId());
            String ifmsc = "";
            switch (enterObj.getIfMacAdmit()) {
                case "0":
                    ifmsc = "否";
                    break;
                case "1":
                    ifmsc = "是";
                    break;
            }
            model.addAttribute("ifmsc", ifmsc);
            List<BisAsnInfo> getList = null;
            if (getObj != null && !"".equals(getObj.getAsnState())) {
                model.addAttribute("asn", getObj);
                getList = asnInfoService.getASNInfoList(asnid);
                model.addAttribute("infolist", getList);
            }
            if (getObj.getIsBonded().equals("0")) {
                model.addAttribute("isBonded", "否");
            } else {
                model.addAttribute("isBonded", "是");
            }
        }
        return "wms/asn/print";
    }
    /**
     * 打印跺卡
     * @param asnid
     * @param model
     * @return
     */
    @RequiresPermissions("bis:asn:print")
    @RequestMapping(value = "printCard/{asnid}", method = RequestMethod.GET)
    public String printCard(@PathVariable("asnid") String asnid, Model model) {
        if (asnid != null && !"".equals(asnid)) {
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BisAsn asn= asnService.get(asnid);
            BisEnterStock enter= enterStockService.get(asn.getLinkId());
            //根据入库联系单以及提单跟箱号获取明细数据
            List<BisEnterStockInfo> listInfo=enterStockInfoService.getEnterInfo(asn.getLinkId(),asn.getBillNum(),asn.getCtnNum());
            StringBuffer sb=new StringBuffer();
            StringBuffer sby=new StringBuffer();
            StringBuffer sbg=new StringBuffer();
            String flag="";
            String flagy="";
            String flagg="";
            for (int i = 0; i<listInfo.size(); i++) {
            	BisEnterStockInfo stInfo=listInfo.get(i);
            	if(stInfo.getCargoName()!=null&&!stInfo.getCargoName().equals(flag)){
            		sb.append(stInfo.getCargoName());
            		if(i!=listInfo.size()-1){
            		  sb.append(",");
            		}
            		flag=stInfo.getCargoName();
            	}
            	if(stInfo.getBgdh()!=null&&!stInfo.getBgdh().equals(flagy)){
            		sby.append(stInfo.getBgdh());
            		if(i!=listInfo.size()-1){
            		  sby.append(",");
            		}
            		flagy=stInfo.getBgdh();
            	}
            	if(stInfo.getYcg()!=null&&!stInfo.getYcg().equals(flagg)){
            		sbg.append(stInfo.getYcg());
            		if(i!=listInfo.size()-1){
            		  sbg.append(",");
            		}
            		flagg=stInfo.getYcg();
            	}
			}
            //获取库存中的数量
            List<Map<String,Object>> list=asnService.getAanT(asnid,null);
            Map<String,Object> zmap=((list!=null&&list.size()>0)?asnService.getAanT(asnid,null).get(0):null);
            //Map<String,Object> cmap=asnService.getAanT(asnid,"12").get(0);
            if(zmap!=null){
	            model.addAttribute("location",zmap.get("CARGO_LOCATION"));
	            model.addAttribute("num",zmap.get("NUM"));
	            model.addAttribute("znet",zmap.get("ZNET"));
            }
            /*if(cmap!=null){
                model.addAttribute("cnum",cmap.get("NUM"));
                model.addAttribute("cgross",cmap.get("ZGROSS"));
            }*/
            /*if(zmap!=null&&cmap!=null){
               model.addAttribute("cha",Double.valueOf(zmap.get("NUM").toString())-Double.valueOf(cmap.get("NUM").toString()));
            }*/
            BisEnterStockInfo enterInfo=new BisEnterStockInfo();
            enterInfo.setCargoName(sb.toString());
            enterInfo.setBgdh(sby.toString());
            enterInfo.setYcg(sbg.toString());
            model.addAttribute("asn",asn);
            model.addAttribute("enter",enter);
            model.addAttribute("enterInfo",enterInfo);
            model.addAttribute("enterTime",enter.getEnterTime()!=null?sdf.format(enter.getEnterTime()):"");
        }
        return "wms/asn/asnPrint";
    }
}

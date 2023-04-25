package com.haiersoft.ccli.api.web;

import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryVo;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.CustomerQueryApplyService;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
public class CustomerQueryApplyController {

    @Autowired
    private CustomerQueryApplyService customerQueryApplyService;

    /**
     * 2.	用户查询申请
     *
     * @param params
     * @return
     */
    @RequestMapping("/api/users/custmerApply")
    @ResponseBody
    public ResponseVo custmerApply(@RequestBody Map<String, String> params) {
        String accountId = params.get("accountId");
        if (StringUtils.isBlank(accountId)) {
            return ResponseVo.error("账户ID不允许为空");
        }

        String customerNumber = params.get("customerNumber");
        String customerName = params.get("customerName");
        if (StringUtils.isBlank(customerNumber) && StringUtils.isBlank(customerName)) {
            return ResponseVo.error("货主账号和货主名称不能都为空");
        }

        String fileUrl = params.get("fileUrl");
        if (StringUtils.isBlank(fileUrl)) {
            return ResponseVo.error("文件上传路径fileUrl不能为空");
        }
        String taxNumber = params.get("taxNumber");


        String remark = params.get("remark");
        String traceId = params.get("traceId");
        //traceId为空时，新增申请记录
        if (StringUtils.isBlank(traceId)) {

//            List<ApiCustomerQueryApply> applyCheckList = customerQueryApplyService.listByTaxNumber(taxNumber);
//            if (!CollectionUtils.isEmpty(applyCheckList)) {
//                return ResponseVo.error("该税号已经申请！");
//            }

            ApiCustomerQueryApply apply = new ApiCustomerQueryApply();
            apply.setAccountId(accountId);
            apply.setCustomerNumber(customerNumber);
            apply.setCustomerName(customerName);
            apply.setTaxNumber(taxNumber);
            apply.setFileUrl(fileUrl);
            apply.setRemark(remark);

            customerQueryApplyService.save(apply);

            return ResponseVo.ok(apply.getId());

        }

        //traceId不为空时，修改原来的申请记录
        else {

            ApiCustomerQueryApply apply = customerQueryApplyService.get(traceId);
            if (null == apply) {
                return ResponseVo.error("traceId对应的记录不存在");
            }
            if(apply.getConfirmStatus()==1){
                return ResponseVo.error("记录已通过，不可修改");
            }
            //如果修改申请时，新传来的税号不等于之前的税号，判断税号是否重复
//            if(!apply.getTaxNumber().equals(taxNumber)){
//                List<ApiCustomerQueryApply> applyCheckList = customerQueryApplyService.listByTaxNumber(taxNumber);
//                if (!CollectionUtils.isEmpty(applyCheckList)) {
//                    return ResponseVo.error("该税号已经申请！");
//                }
//            }
            apply.setAccountId(accountId);
            apply.setCustomerNumber(customerNumber);
            apply.setCustomerName(customerName);
            apply.setTaxNumber(taxNumber);
            apply.setFileUrl(fileUrl);
            apply.setRemark(remark);
            apply.setConfirmStatus(0);
            customerQueryApplyService.update(apply);
            return ResponseVo.ok(apply.getId());
        }

    }

    /**
     * 3.	申请查询
     *
     * @param params
     * @return
     */

    @RequestMapping("/api/users/applyQuery")
    @ResponseBody
    public ResponseVo queryApply(@RequestBody Map<String, String> params) {
        String accountId = params.get("accountId");
        if (StringUtils.isBlank(accountId)) {
            return ResponseVo.error("账户ID不允许为空");
        }
        String taxNumber = params.get("taxNumber");


        List<ApiCustomerQueryApply> list = customerQueryApplyService.findApply(accountId, taxNumber);
//      List<Map<String,String>> list = customerQueryApplyService.findQueryResults(accountId,taxNumber);
//      ApiCustomerQueryVo result = JSONObject.parseObject(JSONObject.toJSONString(list),ApiCustomerQueryVo.class);

        List<ApiCustomerQueryVo> resultList = new ArrayList<>();
        for (ApiCustomerQueryApply apply : list) {
            ApiCustomerQueryVo vo = copyApplyToVo(apply);
            resultList.add(vo);
        }
        return ResponseVo.ok(resultList);


    }

    private ApiCustomerQueryVo copyApplyToVo(ApiCustomerQueryApply apply) {

        ApiCustomerQueryVo vo = new ApiCustomerQueryVo();
        vo.setTraceId(apply.getId());
        vo.setAccountId(apply.getAccountId());

        vo.setCustomerNumber(apply.getCustomerNumber());
        vo.setCustomerName(apply.getCustomerName());
        vo.setTaxNumber(apply.getTaxNumber());
        vo.setFileUrl(apply.getFileUrl());
        vo.setRemark(apply.getRemark());

        vo.setCreateTime(apply.getCreateTime());
        vo.setConfirmStatus(apply.getConfirmStatus());
        vo.setComfirmTime(apply.getComfirmTime());
        vo.setReason(apply.getReason());
        return vo;

    }
}
package com.haiersoft.ccli.cost.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.cost.entity.BisPay;
import com.haiersoft.ccli.cost.service.BisPayMidGroupServeice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @ClassName: BisPayMidGroupController
 * @Description: 费用台账上传到中台 Controller
 * @date 2022年10月13日 下午3:13:31
 */
@Controller
@RequestMapping("cost/bispaymid/")
public class BisPayMidGroupController {
    @Autowired
    private BisPayMidGroupServeice bisPayMidGroupServeice;
    private final static Logger log = LoggerFactory.getLogger(BisPayMidGroupController.class);
    /**
     * 上传 数据到中台 生成结算单
     */
    @RequestMapping(value = "uploadFileToAline/{ids}", method = RequestMethod.POST)
    @ResponseBody
    public String  uploadFileToAline(@PathVariable List<String> ids)  {

        String message = new String();
        try {
            if(!CollectionUtils.isEmpty(ids)){
                message = bisPayMidGroupServeice.subbitJson(ids, true);
            }
        }catch (Exception e){
            log.info(e+"应付报错信息");
        }
        return message;
    }
    /**
     * 上传 数据到中台 不生成结算单
     */
    @RequestMapping(value = "uploadFileToNotAline/{ids}", method = RequestMethod.POST)
    @ResponseBody
    public String  uploadFileToNotAline(@PathVariable List<String> ids)  {
        String message = new String();
        try {
            if (!CollectionUtils.isEmpty(ids)) {
                message = bisPayMidGroupServeice.subbitJson(ids, false);
            }
        }catch (Exception e){
            log.info(e+"应付报错信息");
        }
        return message;
    }



    /*
    * 撤回数据
    * */
    @RequestMapping("returnUpload/{ids}")
    @ResponseBody
    public String returnUpload(@PathVariable List<String> ids){
        String message = new String();
        if(!CollectionUtils.isEmpty(ids)){
            try {
                message = bisPayMidGroupServeice.returnUploadByCodeNum(ids);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return message;
    }
}

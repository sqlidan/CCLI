package com.haiersoft.ccli.cost.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.cost.entity.BisCheckingBook;
import com.haiersoft.ccli.cost.service.StandingBookMidGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.Flags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author
 * @ClassName: StandingBookMidGroupController
 * @Description: 费用台账上传到中台 Controller
 * @date 2022年10月13日 下午3:13:31
 */
@Controller
@RequestMapping("cost/standingbookmid/")
public class StandingBookMidGroupController {

    private static final Logger log = Logger.getLogger("StandingBookMidGroupController");
    @Autowired
    private StandingBookMidGroupService standingBookMidGroupService;
    /**
     * 冷链上传数据到中台进行比对
     * @Param codeNum
     * @throws Exception
     */
    @RequestMapping("uploadFileToAline/{ids}")
    @ResponseBody
    public String uploadFileToAline(@PathVariable List<String> ids){
    	return standingBookMidGroupService.subbitJson(ids, true);
    }
    /**
     * 冷链上传数据到中台进行比对
     * @Param codeNum
     * @throws Exception
     */
    @RequestMapping("uploadFileToNotAline/{ids}")
    @ResponseBody
    public String uploadFileToNotAline(@PathVariable List<String> ids){
        return standingBookMidGroupService.subbitJson(ids, false);
    }

    /**
     * 冷链撤回已经导出到中台的数据
     * @Param codeNum
     * @throws Exception
     */
    @RequestMapping("returnUpload/{ids}")
    @ResponseBody
    public String returnUpload(@PathVariable List<String> ids){
        /**
         * 调用接口 将codeNum 传过去
         */
        String message = null;
        if(!CollectionUtils.isEmpty(ids)){
            message=  standingBookMidGroupService.returnUpload(ids);
            return message;
        }
        return message;
    }

}

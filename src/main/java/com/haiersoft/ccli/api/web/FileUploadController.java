package com.haiersoft.ccli.api.web;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.api.entity.ApiCustomerQueryApply;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.service.CustomerQueryApplyService;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.platform.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Controller
public class FileUploadController {

    @Autowired
    HttpGo httpGo;
    //公网地址
    //private static String url = "https://api.sdland-sea.com/api-lh-oss/lh-oss/uploadFile";
    //内网地址
    private static String url ="http://api.sdlandsea.net/api-lh-oss/lh-oss/uploadFile";

    @RequestMapping("/api/users/uploadFile")
    @ResponseBody
    public ResponseVo upload(@RequestParam("file") MultipartFile multipartFile) {

        String str = HttpUtil.doPostUploadFile(url, multipartFile, "file");
        System.out.println(str);
        Map<String, String> map = JSON.parseObject(str, HashMap.class);
        if (map.get("code").equals("200")) {
            return ResponseVo.ok(map.get("url"));
        } else {
            return ResponseVo.error("上传失败");
        }
    }

}

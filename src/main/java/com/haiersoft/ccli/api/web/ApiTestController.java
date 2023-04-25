package com.haiersoft.ccli.api.web;

import com.haiersoft.ccli.api.entity.PledgeRequestVo;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.supervision.dao.ApprGnoSequenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 86185
 * @Date 2022/5/27 10:11
 * @Version 1.0
 */
@RestController
public class ApiTestController {

    @Autowired
    ApprGnoSequenceDao apprGnoSequenceDao;

    @RequestMapping("/api/users/test")
    @ResponseBody
    public ResponseVo staticSave(){
        System.out.println(apprGnoSequenceDao.getNextval());
        return ResponseVo.error(apprGnoSequenceDao.getNextval());

    }
}

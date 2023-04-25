package com.haiersoft.ccli.remoting.hand.users.service;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.common.utils.Result;
import com.haiersoft.ccli.remoting.hand.users.service.UserWebService;
import com.haiersoft.ccli.system.entity.Permission;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.PermissionService;
import com.haiersoft.ccli.system.service.UserService;

/**
 * @author Connor.M
 * @ClassName: UserWebService
 * @Description: 手持的User的WebService
 * @date 2016年3月3日 上午9:58:06
 */
@WebService
@Service
public class UserWebService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * @return
     * @throws
     * @author Connor.M
     * @Description: 测试连接接口
     * @date 2016年3月3日 上午10:16:53
     */
    public String testConnecttion() {
        return "OK";
    }

    /**
     * @param loginName
     * @param passWord
     * @return
     * @throws
     * @author Connor.M
     * @Description: 用户登录接口
     * @date 2016年3月3日 上午10:18:49
     */
    public String userLogin(String loginName, String passWord) {
        Result<User> result = new Result<User>();
        User user = userService.getUser(loginName);
        if (user != null) {
            boolean flag = userService.checkPassword(user, passWord);
            if (flag) {
                //用户手持权限
                List<Permission> permissionList = permissionService.findHand(loginName);
                if (!permissionList.isEmpty()) {
                    List<String> codeList = new ArrayList<String>();
                    for (Permission permission : permissionList) {
                        codeList.add(permission.getPermCode());
                    }
                    user.setUserRoles(null);//角色权限 制空 json解析有问题
                    result.setObj(user);
                    result.setList(codeList);
                    result.setCode(0);
                    result.setMsg("登录成功！");
                } else {
                    result.setCode(1);
                    result.setMsg("此用户没有手持权限！");
                }
            } else {
                result.setCode(1);
                result.setMsg("用户密码错误！");
            }
        } else {
            result.setCode(1);
            result.setMsg("用户不存在！");
        }
        return JSON.toJSONString(result);
    }
}

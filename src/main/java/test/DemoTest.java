package test;

import com.haiersoft.ccli.cost.dao.BaseExpenseCategoryDetailDao;
import com.haiersoft.ccli.cost.entity.enumVo.CostClassifyEnum;
import com.haiersoft.ccli.cost.service.BaseExpenseCategoryDetailService;
import com.haiersoft.ccli.cost.service.BisPayMidGroupServeice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

public class DemoTest {

    public static void main(String[] args) {
        String 单证费 = CostClassifyEnum.getMsgByCode("");
        System.out.println(单证费);
    }
}

package com.haiersoft.ccli.junit;
import java.util.Date;
import javax.jws.WebService;
@WebService(targetNamespace = "http://ws.cold.qdport.com/")
public interface CustomerService {
    /**
     * 客户信息添加
     * @Title: addInfo 
     * @return 
     * @author luls
     * @since 2017年6月1日 V 1.0
     */
     String addInfo(int ids,
                    String clientCode,
                    String clientName,
                    String clientSort,
                    String contactMan,
                    String telNum,
                    String address,
                    String eMail,
                    String rmbBank,
                    String rmbAccount,
                    String usdBank,
                    String usdAccount,
                    String taxAccount,
                    int checkDay,
                    String taxpayer,
                    String useUnit,
                    String saler,
                    String pledgeType,
                    int delFlag,
                    String cwnum,
                    String gysnum,
                    int serviceId,
                    String limits,
                    String clientLevel,
                    String note);
        
    /**
     * 客户信息更新
     * @Title: addInfo 
     * @return 
     * @author luls
     * @since 2017年6月1日 V 1.0
     */        
     String editInfo(int ids, String clientCode, String clientName, String clientSort,
                     String contactMan, String telNum, String address, String eMail, String rmbBank,
                     String rmbAccount, String usdBank, String usdAccount, String taxAccount, int checkDay,
                     String taxpayer, String useUnit, String saler, String pledgeType, int delFlag,
                     String cwnum, String gysnum, int serviceId, String limits, String clientLevel,
                     String note);
     /**
      * 客户信息删除
      * @Title: deleteInfo 
      * @param ids
      * @return 
      * @author luls
      * @since 2017年6月2日 V 1.0
      */
     String deleteInfo(int ids);
    
    /**
     * 预约时间同步--新增
     * @Title: addTime 
     * @param id
     * @param orderTime
     * @param mosttimes
     * @param note
     * @param alreadyTimes
     * @return 
     * @author luls
     * @since 2017年6月2日 V 1.0
     */
     String addTime(String id, String orderTime, int mosttimes, String note);
    /**
     * 预约时间同步--修改
     * @Title: editTime 
     * @param id
     * @param orderTime
     * @param mosttimes
     * @param note
     * @param alreadyTimes
     * @return 
     * @author luls
     * @since 2017年6月2日 V 1.0
     */
     String editTime(String id, String orderTime, int mosttimes, String note);
    /**
     * 预约时间同步--删除
     * @Title: deleteTime 
     * @param id
     * @return 
     * @author luls
     * @since 2017年6月2日 V 1.0
     */
     String deleteTime(String id);
     /**
      * 预约审核同步
      * @Title: updateExamine 
      * @param code
      * @param audittime
      * @param auditperson
      * @param state
      * @param workTime
      * @return 
      * @author luls
      * @since 2017年6月2日 V 1.0
      */
     String updateExamine(String bill, Date audittime, String auditperson, String state,
                          String workTime);
     /**
      * 产品类型新增
      * @Title: addProduct 
      * @param id
      * @param pname
      * @param printid
      * @return 
      * @author luls
      * @since 2017年6月5日 V 1.0
      */
     String addProduct(int id, String pname, int printid); 
     
     String editProduct(int id, String pname, int printid); 
     
     String deleteProduct(int id);
    
}

package com.haiersoft.ccli.platform.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.platform.dao.ReservationOutboundDao;
import com.haiersoft.ccli.platform.entity.PlatformReservationOutbound;
import com.haiersoft.ccli.supervision.web.FTPUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author A0047794
 * @日期 2021/11/28
 * @描述
 */
@Service
@Transactional(readOnly = true)
public class ReservationOutboundService extends BaseService<PlatformReservationOutbound,String> {
   @Autowired
   private ReservationOutboundDao dao;
    @Override
    public HibernateDao<PlatformReservationOutbound, String> getEntityDao() {
        return dao;
    }

    //根据id更新记录中的maniFestId，同时更新localstauts状态
    public void updateManiFestIdById(String maniFestId, String localStatus,String id) {
        dao.updateManiFestIdbyId(maniFestId, localStatus,id);

    }

    //根据id反写生成的xml文件，保存文件名
    public void updateFiledNameById(String fileName, String id) {
        dao.updateFiledNameById(fileName,id);
    }

    /**
     * 根据file名称 修改预约出库申报状态
     * @param fileName
     * @param isSuccess
     */
    public void updateIsSuccessByFiledName(String fileName, String isSuccess) {
        dao.updateIsSuccessByFiledName(fileName,isSuccess);
    }


    @Transactional
    public void getXml() {

        List<PropertyFilter> filters =this.getFilters();
        //获取当天，已申报的 所有预约出库记录
        List<PlatformReservationOutbound> outboundList = this.search(filters);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(new Date());
        // 测试用
        String fljgPath = "SendError/20230406";
        FTPUtils ftpUtils = FTPUtils.getInstance();

        // 获取路径下所有的回执文件
        FTPFile[] fileList = ftpUtils.getFilesList(fljgPath);

        for (FTPFile ftpFile : fileList) {
            String fileName = ftpFile.getName();
            String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!"FLJGSX".equals(substring)) {
                continue;
            }
            // 读取回执
            InputStream in = ftpUtils.retrieveFile(ftpFile.getName(), "/SendError/20230406/", fileName);
            String response = new BufferedReader(new InputStreamReader(in))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            if (StringUtils.isNotBlank(response)) {
                // xml转json
                Document docResult = XmlUtil.readXML(response);
                // 获取回执的类型
                Object xPath = XmlUtil.getByXPath("//APP_STEP_ID", docResult, XPathConstants.STRING);

                String xPathStr = xPath.toString();

                Map map = XmlUtil.xmlToMap(response);
                System.out.println(">>>>>>>>>>>>>>>>>>>" + map);
                Map NonbondedResponseMessage = (Map) map.get("SDEPORT_DATA");
                //转化为json
                String s = JSON.toJSONString(NonbondedResponseMessage);
                JSONObject jsonObject = JSONObject.parseObject(s);
                Map NonbondedResponseMessage1 = (Map) jsonObject.get("NonbondedResponseMessage");
                Map QdcdcResponse = (Map) NonbondedResponseMessage1.get("QdcdcResponse");
                if (QdcdcResponse != null) {
                    String rmk = QdcdcResponse.get("rmk").toString();
                    //反写预约出库明细 核放申请成功状态
                    int firstIndex = fileName.indexOf("L");
                    int lastIndex = fileName.indexOf(".");
                    String lastName =  fileName.substring(firstIndex,lastIndex);
                    if(rmk.contains("成功")) {
                        this.updateIsSuccessByFiledName(lastName,"1");
                    } else {
                        this.updateIsSuccessByFiledName(lastName,"2");
                    }
                }

            }
        }
    }

    /**
     * 查询预约出库   添加过滤条件
     * @return
     */
    public List<PropertyFilter> getFilters() {
        List<PropertyFilter> filters = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        //结束时间
        Calendar end = Calendar.getInstance();
        start.add(Calendar.DATE, 0);
        // 时
        start.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        start.set(Calendar.MINUTE, 0);
        // 秒
        start.set(Calendar.SECOND, 0);

        start.set(Calendar.MILLISECOND, 0);


        end.add(Calendar.DATE, 1);
        // 时
        end.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        end.set(Calendar.MINUTE, 0);
        // 秒
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        //预约日期为当天的
        filters.add(new PropertyFilter("GED_appointDate", start.getTime()));
        filters.add(new PropertyFilter("LTD_appointDate", end.getTime()));
        //报文状态为已申报
        filters.add( new PropertyFilter("EQS_isSuccess", "0"));

        return filters;
    }


    @Transactional
    public void getXml2()  {


        /**
         * 本地测试用
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(new Date());
        // 测试用
        // 获取路径下所有的回执文件
        File file = new File("D:/supervision/Receive");
        File[] fileList = file.listFiles();

        for (File file1 : fileList) {
            String fileName = file1.getName();
            String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!"FLJGSX".equals(substring)) {
                continue;
            }
            try{
                InputStream is = new FileInputStream(file1);
                // 读取回执
                String response = new BufferedReader(new InputStreamReader(is))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
                if (StringUtils.isNotBlank(response)) {
                    // xml转json
                    Document docResult = XmlUtil.readXML(response);
                    // 获取回执的类型
                    Object xPath = XmlUtil.getByXPath("//APP_STEP_ID", docResult, XPathConstants.STRING);

                    String xPathStr = xPath.toString();

                    Map map = XmlUtil.xmlToMap(response);
                    System.out.println(">>>>>>>>>>>>>>>>>>>" + map);
                    Map NonbondedResponseMessage = (Map) map.get("SDEPORT_DATA");
                    //转化为json
                    String s = JSON.toJSONString(NonbondedResponseMessage);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    Map NonbondedResponseMessage1 = (Map) jsonObject.get("NonbondedResponseMessage");
                    Map QdcdcResponse = (Map) NonbondedResponseMessage1.get("QdcdcResponse");
                    if (QdcdcResponse != null) {
                        String rmk = QdcdcResponse.get("rmk").toString();
                        //反写预约出库明细 核放申请成功状态
                        int firstIndex = fileName.indexOf("L");
                        int lastIndex = fileName.indexOf(".");
                        String lastName =  fileName.substring(firstIndex,lastIndex);
                        if(rmk.contains("成功")) {
                            this.updateIsSuccessByFiledName(lastName,"1");
                        } else {
                            this.updateIsSuccessByFiledName(lastName,"2");
                        }

                    }

                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }

    /**
     * 得到文件名称
     *
     * @param path 路径
     * @return {@link List}<{@link String}>
     */
    private List<String> getFileNames(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        return getFileNames(file, fileNames);
    }

    /**
     * 得到文件名称
     *
     * @param file      文件
     * @param fileNames 文件名
     * @return {@link List}<{@link String}>
     */
    private List<String> getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileNames(f, fileNames);
            } else {
                fileNames.add(f.getName());
            }
        }
        return fileNames;
    }


}

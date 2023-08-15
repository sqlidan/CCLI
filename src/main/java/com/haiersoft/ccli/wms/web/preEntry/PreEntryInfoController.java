package com.haiersoft.ccli.wms.web.preEntry;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.persistence.PropertyFilter;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.supervision.tempuri.ReturnModel;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.utils.UserUtil;
import com.haiersoft.ccli.wms.entity.apiEntity.*;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntry;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfo;
import com.haiersoft.ccli.wms.entity.preEntry.BisPreEntryInfoDJ;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoDJService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryInfoService;
import com.haiersoft.ccli.wms.service.preEntry.PreEntryService;
import org.apache.commons.collections.ArrayStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName: PreEntryInfoController
 * @Description: 预报单货物明细Controller
 */
@Controller
@RequestMapping("wms/preEntryInfo")
public class PreEntryInfoController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PreEntryInfoController.class);

    @Autowired
    private PreEntryService preEntryService;
    @Autowired
    private PreEntryInfoService preEntryInfoService;
    @Autowired
    private PreEntryInfoDJService preEntryInfoDJService;

    private static final String memberCode = "eimskipMember";
    private static final String pass = "66668888";
    private static final String icCode = "2100030046252";

    /**
     * 货物信息列表
     */
    @RequestMapping(value = "json/{forId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getData(HttpServletRequest request, @PathVariable("forId") String forId) {
        Page<BisPreEntryInfo> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        PropertyFilter filter = new PropertyFilter("EQS_forId", forId);
        filters.add(filter);
        page = preEntryInfoService.search(page, filters);
        return getEasyUIData(page);

    }

    /**
     * 添加货物信息
     */
    @RequestMapping(value = "create/{forId}", method = RequestMethod.POST)
    @ResponseBody
    public String create(@Valid BisPreEntryInfo bisPreEntryInfo, @PathVariable("forId") String forId, Model model, HttpServletRequest request) {
        bisPreEntryInfo.setForId(forId);
        User user = UserUtil.getCurrentUser();
        bisPreEntryInfo.setCreateBy(user.getName());//创建人
        bisPreEntryInfo.setCreateTime(new Date());//创建时间
        preEntryInfoService.save(bisPreEntryInfo);
        return "success";
    }

    /**
     * 删除货物信息
     */
    @RequestMapping(value = "deleteinfo/{ids}")
    @ResponseBody
    public String deleteinfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            preEntryInfoService.delete(ids.get(i));
        }
        return "success";
    }

    /**
     * 随附单据列表
     */
    @RequestMapping(value = "jsonDJ/{forId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getDJData(HttpServletRequest request, @PathVariable("forId") String forId) {
        Page<BisPreEntryInfoDJ> page = getPage(request);
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);

        PropertyFilter filter = new PropertyFilter("EQS_forId", forId);
        filters.add(filter);
        page = preEntryInfoDJService.search(page, filters);
        return getEasyUIData(page);

    }

    /**
     * 打开随附单据上传弹窗
     */
    @RequestMapping(value = "createDJ/{forId}", method = RequestMethod.GET)
    public String createDJ(Model model, @PathVariable("forId") String forId) {
        model.addAttribute("forId", forId);
        return "wms/preEntry/preEntryBGHManagerUpload";
    }

    /**
     * 添加随附单据
     */
    @RequestMapping(value = "createDJFile", method = RequestMethod.POST)
    @ResponseBody
    public String createDJFile(@RequestParam("file") MultipartFile multipartFile,@RequestParam("remark") String remark, @RequestParam("forId") String forId, HttpServletRequest request) {
        // 判断文件是否为空
        if (!multipartFile.isEmpty()) {
            try {
                User user = UserUtil.getCurrentUser();
                String fileName = multipartFile.getOriginalFilename();
                String fileSize = formatSize(multipartFile.getSize());
                //把文件MultipartFile转为File
                File file = MultipartFileToFile(multipartFile);
                byte[] fileContent = File2byte(file);
                //保存
                int result = preEntryInfoDJService.saveDJ(forId,user.getName(),fileName,fileSize,fileContent,remark);
                if(result > 0){
                    return "success";
                }else{
                    return "上传失败";
                }
            } catch (Exception e) {
                return e.getMessage();
            }
        } else {
            return "上传不能为空";
        }
    }
    /**
     * 删除随附单据
     */
    @RequestMapping(value = "deleteDJinfo/{ids}")
    @ResponseBody
    public String deleteDJinfo(@PathVariable("ids") List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            preEntryInfoDJService.delete(ids.get(i));
        }
        return "success";
    }
    /**
     * 提交随附单据
     */
    @RequestMapping(value = "submitDJinfo/{forId}")
    @ResponseBody
    public String submitDJinfo(@PathVariable("forId") String forId) {
        BisPreEntry bisPreEntry = preEntryService.get(forId);
        if(bisPreEntry!=null){
            List<BisPreEntryInfoDJ> bisPreEntryInfoDJList = preEntryInfoDJService.getList(bisPreEntry.getForId());
            if(bisPreEntryInfoDJList == null || bisPreEntryInfoDJList.size() == 0){
                return "暂无随附数据，无需提交。";
            }else{
                Map<String,Object> resultMap =  uploadBisPreEntryInfoDJList(bisPreEntry,bisPreEntryInfoDJList);
                return resultMap.get("msg").toString();
            }
        }
        return "未获取到预录入信息";
    }


//======================================================================================================================
    //上传随附单据
    public Map<String,Object> uploadBisPreEntryInfoDJList(BisPreEntry bisPreEntry,List<BisPreEntryInfoDJ> bisPreEntryInfoDJList) {
        List<CNemsAcmpRlType> cNemsAcmpRlTypeList = new ArrayStack();
        Map<String,Object> resultMap = new HashMap<>();
        // 随附单证数据列表
        List<SasAcmpRLType> sasAcmpRLList = new ArrayList<>();
        List<FileMessage> files = new ArrayList<>();
        // 预录入统一编号
        String dclSeqNo = bisPreEntry.getSeqNo();
        //1-资质申请 2-账册 3-报核 4－清单 5－质疑
        String blsType = "4";
        int no = 1;
        for (BisPreEntryInfoDJ bisPreEntryInfoDJ : bisPreEntryInfoDJList) {
            FileMessage fileMessage = new FileMessage();
            fileMessage.setInvtGdsSeqNo(null);
            fileMessage.setDclSeqNo(dclSeqNo);
            fileMessage.setModfMarkCD("3");//0-未修改 1-修改  3-增加
            fileMessage.setAcmpFormSeqno(no+"");
            fileMessage.setAppCode("QP");
            fileMessage.setReplace("N");
            fileMessage.setAcmpFormTypeCd("FILE");
            fileMessage.setAcmpFormFileNm(bisPreEntryInfoDJ.getFileName());
            fileMessage.setIcCardNo(icCode);
            fileMessage.setTransferTradeCode(bisPreEntry.getLRDWBM());
            // 按照规则拼接上传文件名 42507EMS000B42502100000000001001.pdf
            // 主管关区代码
            String dclPlcCuscd = bisPreEntry.getZGHG();
            String num = "0";
            int size = no;
            if (size < 10) {
                num = num + "0" + size;
            } else {
                num = num + size;
            }
            String fileName = dclPlcCuscd + blsType + "EMS000" + dclSeqNo + num + ".pdf";
            fileMessage.setFileName(fileName);
            //将随附单据转成Base64
            byte[] fileContent = bisPreEntryInfoDJ.getFileContent();
            String ss = Base64.getEncoder().encodeToString(fileContent);
            fileMessage.setFileContent(ss);
            fileMessage.setFileType("pdf");
            files.add(fileMessage);

            SasAcmpRLType sasAcmpRLType = new SasAcmpRLType();
            sasAcmpRLType.setAcmpFormFmt("2");//1－结构化 2－非结构化
            sasAcmpRLType.setInvtGdsSeqNo(null);
            sasAcmpRLType.setBlsType(blsType);
            sasAcmpRLType.setIcCardNo(fileMessage.getIcCardNo());
            sasAcmpRLType.setAcmpFormFileNm(fileMessage.getFileName());
            sasAcmpRLType.setTransferTradeCode(fileMessage.getTransferTradeCode());
            sasAcmpRLType.setChgTmsCnt("0");//默认为0，变更修改时需填写为对应第几次变更（须与表头变更次数保持一致）
            sasAcmpRLType.setBlsNo(dclSeqNo);
            sasAcmpRLType.setAcmpBlsStatus("9");
            sasAcmpRLType.setFileName(fileMessage.getFileName());
            sasAcmpRLType.setIndbTime(null);
            sasAcmpRLType.setModfMarkCD(fileMessage.getModfMarkCD());//0-未修改 1-修改  3-增加
            sasAcmpRLType.setSeqNo(null);
            sasAcmpRLType.setAcmpFormSeqNo(fileMessage.getAcmpFormSeqno());//选填
            sasAcmpRLType.setAcmpFormTypeCD(fileMessage.getAcmpFormTypeCd());//选填 R-减免税证明 FILE-文件
            sasAcmpRLList.add(sasAcmpRLType);

            no++;
        }
        if (CollectionUtil.isNotEmpty(files)) {
            // 调用关务平台附件上传接口
            FileUploadRequest fileUploadRequest = new FileUploadRequest();
            fileUploadRequest.setFiles(files);
            fileUploadRequest.setMemberCode(memberCode);
            fileUploadRequest.setPass(pass);
            fileUploadRequest.setIcCode(icCode);
            Map<String,Object> fileUploadMap = FileUpload(fileUploadRequest);
            if ("200".equals(fileUploadMap.get("code").toString())) {
                System.out.println(fileUploadMap.get("data").toString());
            }else{
                return fileUploadMap;
            }
        }else{
            resultMap.put("code","500");
            resultMap.put("msg","随附单据上传平台失败");
            return resultMap;
        }
        if (CollectionUtil.isNotEmpty(sasAcmpRLList)) {
            // 随附单据数据
            SasAcmpRLMessage sasAcmpRLMessage = new SasAcmpRLMessage();
            sasAcmpRLMessage.setSasAcmpRLList(sasAcmpRLList);
            sasAcmpRLMessage.setBlsNo(dclSeqNo);
            sasAcmpRLMessage.setBlsType(blsType);
            sasAcmpRLMessage.setChgTmsCnt("0");
            sasAcmpRLMessage.setMemberCode(memberCode);
            sasAcmpRLMessage.setPass(pass);
            sasAcmpRLMessage.setIcCode(icCode);
            Map<String,Object> sasCommonSeqNoRequestMap = AcmpRLSaveService(sasAcmpRLMessage);
            if ("200".equals(sasCommonSeqNoRequestMap.get("code").toString())) {
                System.out.println(sasCommonSeqNoRequestMap.get("data").toString());
                return sasCommonSeqNoRequestMap;
            }
            return sasCommonSeqNoRequestMap;
        }else{
            resultMap.put("code","500");
            resultMap.put("msg","随附单据上传申报失败");
            return resultMap;
        }

    }

//======================================================================================================================
    //获取文件大小
    private String formatSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    //把文件MultipartFile转为File
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将文件转换成byte数组
     */
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
//======================================================================================================================
    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.ReturnModel>
     * @Author chenp
     * @Description 随附单据上传接口
     * @Date 15:42 2021/3/16
     * @Param [fileUploadRequest]
     **/
    public Map<String,Object> FileUpload(FileUploadRequest fileUploadRequest) {
        Map<String,Object> resultMap = new HashMap<>();
        ReturnModel returnModel = new ReturnModel();
        String dataStr = "";
        try {
            fileUploadRequest.setKey(ApiKey.保税监管_随附单据上传秘钥.getValue());
//			String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_文件上传接口.getValue(), JSON.toJSONString(fileUploadRequest));
            String s = HttpUtils.HttpPostWithJson("http://customsapi.sdlandsea.net/api/bondedApi/FileUpload", JSON.toJSONString(fileUploadRequest));
            //{"code":200,"msg":null,"data":{"returnCode":0,"msg":null,"obj":null,"responseObject":"{\"ReturnCode\":0,\"Msg\":null,\"Obj\":null}"}}
            System.out.println("随附单据上传接口结果："+s);
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                if (data != null) {
                    dataStr = data.toString();
                }
                returnModel = JSON.toJavaObject(JSON.parseObject(dataStr), ReturnModel.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",returnModel);
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_随附单据上传秘钥","结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }

    /**
     * @return com.chenay.core.tool.api.R<com.chenay.bean.entity.customs.SasCommonSeqNoRequest>
     * @Author chenp
     * @Description //TODO
     * @Date 15:43 2021/3/16
     * @Param [sasAcmpRLMessage]
     **/
    public Map<String,Object> AcmpRLSaveService(SasAcmpRLMessage sasAcmpRLMessage) {
        Map<String,Object> resultMap = new HashMap<>();
        SasCommonSeqNoRequest sasCommonSeqNoRequest = new SasCommonSeqNoRequest();
        String dataStr = "";
        try {
            sasAcmpRLMessage.setKey(ApiKey.保税监管_随附单据上传秘钥.getValue());
            String s = HttpUtils.HttpPostWithJson(ApiType.保税监管_随附单据数据保存服务接口.getValue(), JSON.toJSONString(sasAcmpRLMessage));
            //{"code":200,"msg":null,"data":{"blsNo":null,"blsType":null,"chgTmsCnt":null,"operCusRegCode":null,"rltGdsSeqno":null,"seqNo":null,"securityModel":null,"memberCode":null,"pass":null,"icCode":null}}
            System.out.println("随附单据数据保存服务接口结果："+s);
            JSONObject jsonObject = JSON.parseObject(s);
            String code = jsonObject.get("code") == null ? "500" : jsonObject.get("code").toString();
            if ("200".equals(code)) {
                Object data = jsonObject.get("data");
                if (data != null) {
                    dataStr = data.toString();
                }
                sasCommonSeqNoRequest = JSON.toJavaObject(JSON.parseObject(dataStr), SasCommonSeqNoRequest.class);
                resultMap.put("code","200");
                resultMap.put("msg","success");
                resultMap.put("data",sasCommonSeqNoRequest);
            } else {
                Object data = jsonObject.get("msg");
                if (data != null) {
                    dataStr = data.toString();
                }
                logger.error("保税监管_随附单据数据保存服务接口","结果"+dataStr);
                logger.error(dataStr);
                resultMap.put("code","500");
                resultMap.put("msg",dataStr);
                resultMap.put("data",null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return resultMap;
    }
}

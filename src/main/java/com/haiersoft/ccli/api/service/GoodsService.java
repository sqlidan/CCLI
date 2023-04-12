package com.haiersoft.ccli.api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.haiersoft.ccli.api.constant.OpenGateConstant;
import com.haiersoft.ccli.api.util.OpenGateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.api.dao.GoodsDao;
import com.haiersoft.ccli.api.entity.GoodsResponseTransferVO;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.common.utils.HttpGo;

@Service
public class GoodsService extends BaseService<BaseProductClass, Integer> {
    //String url="http://211.97.194.78:10883/api/coldchain/goods-type-syn";
    private static String url = OpenGateConstant.OPEN_GATE_GOODS_TYPE_SYN_URL;
    private static final Logger log = LoggerFactory.getLogger(GoodsService.class);
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private HttpGo httpGo;

    @Override
    public HibernateDao<BaseProductClass, Integer> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 按id值查询货品小类表 同步货种小类
     *
     * @param id id值
     */
    @Async
    public void getInfoById(Integer id) {
        if (id == null) {
            return;
        }
		BaseProductClass baseProductClass =  goodsDao.Query(id);
		if (baseProductClass == null) {
			return;
		}
		//请求head部分
		Map<String,String> header = OpenGateUtil.headerBuilder();
		//请求body部分
		String paramJson = bodyJsonBuild(baseProductClass);
		log.error("货种小类信息同步:{}",paramJson);
		String resultJson = httpGo.sendRequest(url,header,paramJson);
		if (resultJson == null) {
			log.error("传输失败，没有返回值");
		} else {
			log.error("resultJson:{}",resultJson);
		}
    }

    private static String bodyJsonBuild(BaseProductClass baseProductClass){
		//请求body部分
		//注意这里的body部分分为head和body 两段
		Map<String,Object> header = new HashMap<>();
		header.put("accessToken", OpenGateUtil.getToken());
		header.put("appId", OpenGateConstant.APP_ID);
		header.put("appSecret",OpenGateConstant.APP_SECRET);
		header.put("charSet", OpenGateConstant.CHAR_SET);
		String requestId = UUID.randomUUID().toString();
		header.put("requestId",requestId );
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String dateStr = sdf.format(new Date());
		header.put("requestTime",dateStr);

		header.put("userId",OpenGateConstant.USER_ID);
		header.put("version", OpenGateConstant.VERSION);
		header.put("channel",OpenGateConstant.CHANNEL);

		Map<String,Object> body = new HashMap<>();
		body.put("traceId",requestId);
		body.put("channel",OpenGateConstant.CHANNEL);
		body.put("operationUserId",OpenGateConstant.USER_ID);
		body.put("requestTime",dateStr);

		Map<String, String> coldchainGoodsTypeMap = new HashMap<>();
		coldchainGoodsTypeMap.put("typeCode", baseProductClass.getId().toString());
		coldchainGoodsTypeMap.put("typeName", baseProductClass.getpName());

		List<Map<String, String>> typeList = new ArrayList<>();
		typeList.add(coldchainGoodsTypeMap);
		body.put("coldchainGoodsType",typeList);

		Map<String,Map<String,Object>> param = new HashMap<>();
		param.put("header",header);
		param.put("body",body);
		String paramJson = JSON.toJSONString(param);

		return paramJson;
	}

	public static void main(String[] args) {
		BaseProductClass baseProductClass = new BaseProductClass();
		baseProductClass.setId(123);
		baseProductClass.setpName("defgsef");

		String param = bodyJsonBuild(baseProductClass);
		System.out.println(param);
	}

    /**
     * 按List值查询货品小类表
     *
     * @param list list值存储的是id值
     */

    public void getInfoByList(List list) {
        String resultPost = null;
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GoodsResponseTransferVO goodsTransferResponseVO = new GoodsResponseTransferVO();
        List<Map<String, String>> listMap = new ArrayList<>();
        if (list != null) {
            Map<String, String> map = new HashMap<String, String>();
            listMap = goodsInfoList(list);
            if (listMap != null) {
                goodsTransferResponseVO.setColdchainGoodsType(listMap);
                goodsTransferResponseVO.setTraceId(uuidStr);
                goodsTransferResponseVO.setChannel("0");
                goodsTransferResponseVO.setRequestTime(df.format(new Date()));
                goodsTransferResponseVO.setOperationUserId("100000000");
                String resultJson = JSON.toJSONString(goodsTransferResponseVO);
                resultPost = httpGo.sendRequest(url, resultJson);
                if (resultPost == null) {
                    log.error("传输失败，没有返回值");
                } else {
                    log.info(resultPost);
                }
            }

        }
    }

    @Transactional
	List<Map<String, String>> goodsInfoList(List list) {
        if (list != null) {
            List<Map<String, String>> listMap = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                BaseProductClass baseProductClass = new BaseProductClass();
                baseProductClass = goodsDao.Query((Integer) list.get(i));
                if (baseProductClass != null) {
                    map.put("typeCode", baseProductClass.getId().toString());
                    map.put("typeName", baseProductClass.getpName());
                    listMap.add(map);
                }
            }
            return listMap;
        }
        return null;
    }


}

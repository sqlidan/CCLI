package com.haiersoft.ccli.api.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.api.entity.GoodsRequestTransferVO;
import com.haiersoft.ccli.api.entity.GoodsResponseTransferVO;
import com.haiersoft.ccli.api.service.GoodsService;
import com.haiersoft.ccli.base.entity.BaseProductClass;
import com.haiersoft.ccli.common.utils.HttpGo;
import com.haiersoft.ccli.common.web.BaseController;
import com.haiersoft.ccli.system.service.ScheduleJobService;

/**
 * 用户接口controller
 *
 * @author 吕术翰
 * @date 2020年9月9日
 */
@Controller
@RequestMapping("api/users")
public class GoodsController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private HttpGo httpGo;

	/**
	 * 接口进入方法
	 */
	@RequestMapping(value = "type-of-goods", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	@ResponseBody
	public void getData(@RequestBody GoodsRequestTransferVO goodsTransferVO) {
		String resultPost = null;
		//String url = "http://211.97.194.78:10883/api/coldchain/goods-type-syn";
		String url="";
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		BaseProductClass baseProductClass = new BaseProductClass();
		GoodsResponseTransferVO goodsTransferResponseVO = new GoodsResponseTransferVO();
		Integer id = null;
		if (goodsTransferVO != null) {
			id = goodsTransferVO.getId();
			/* baseProductClass=goodsService.Query(id); */
			if (baseProductClass != null) {
				List<Map<String, String>> listMap = new ArrayList<>();
				Map<String, String> map = new HashMap<String, String>();
				goodsTransferResponseVO.setTraceId(uuidStr);
				goodsTransferResponseVO.setChannel("0");
				goodsTransferResponseVO.setRequestTime(df.format(new Date()));
				goodsTransferResponseVO.setOperationUserId("100000000");
				map.put("typeCode", baseProductClass.getId().toString());
				map.put("typeName", baseProductClass.getpName());
				listMap.add(map);
				goodsTransferResponseVO.setColdchainGoodsType(listMap);
				String resultJson = JSON.toJSONString(goodsTransferResponseVO);
				resultPost = httpGo.sendRequest(url, resultJson);
				if (resultPost == null) {
					log.error("传输失败，没有返回值");
				}
			}
		}

	}

}

package com.haiersoft.ccli.outsidequery.web;

import com.haiersoft.ccli.outsidequery.entity.ResponseVo;
import com.haiersoft.ccli.outsidequery.entity.OutsideInvQuery;
import com.haiersoft.ccli.outsidequery.entity.OutsideQuery;
import com.haiersoft.ccli.outsidequery.service.OutsideQueryService;

import bsh.This;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 *
 */

@RequestMapping(value = "/outsidequery")
@Controller
public class OutsideQueryController {

	@Autowired
	private OutsideQueryService outsideQueryService;
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public ResponseVo test()  {
		return ResponseVo.success();

	}

	/**
	 * 库存查询
	 */

	@RequestMapping(value = "/inv", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo inv(@RequestBody(required=false) Map<String, String> params) throws ParseException {


		//如果params为空或startTime，endTime同时为空 时，默认查询日期
		if(null==params || (StringUtils.isEmpty(params.get("startTime")) && StringUtils.isEmpty(params.get("endTime")))) {
			params = new HashMap<String, String>();
			params.put("defaultTime","1");
			
			

			return ResponseVo.ok(this.calcOutsideInvQuery(params));
		}
		
		//params不为空时检查日期区间
		String chkMsg = this.checkDuration(params);
		if(!("0".equals(chkMsg))) {
			return ResponseVo.error(chkMsg);
		}
		

		return ResponseVo.ok(this.calcOutsideInvQuery(params));

	}
	
	//库存查询返回值中增加总件数 totalPieces
	private OutsideInvQuery calcOutsideInvQuery(Map<String, String> params) {
		List<OutsideQuery> list = outsideQueryService.getStocks(params);
		OutsideInvQuery invQuery = new OutsideInvQuery();
		invQuery.setData(list);
		invQuery.setTotalPieces(list.stream().mapToInt(OutsideQuery::getPieces).sum());
		return invQuery;
	}

	/**
	 * 入库查询
	 */

	@RequestMapping(value = "/enter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo enter(@RequestBody(required=false) Map<String, String> params) throws ParseException {
		
		//如果params为空或startTime，endTime同时为空 时，默认查询日期
		if(null==params || (StringUtils.isEmpty(params.get("startTime")) && StringUtils.isEmpty(params.get("endTime")))) {
			params = new HashMap<String, String>();
			params.put("defaultTime","1");
			return ResponseVo.ok(outsideQueryService.getEnterStockStocks(params));
		}
		
		//params不为空时检查日期区间
		String chkMsg = this.checkDuration(params);
		if(!("0".equals(chkMsg))) {
			return ResponseVo.error(chkMsg);
		}
		
		List<OutsideQuery> list = outsideQueryService.getEnterStockStocks(params);

		return ResponseVo.ok(list);

	}

	/**
	 * 出库查询
	 */

	@RequestMapping(value = "/out", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo out(@RequestBody(required=false) Map<String, String> params) throws ParseException {
		
		//如果params为空或startTime，endTime同时为空 时，默认查询日期
		if(null==params || (StringUtils.isEmpty(params.get("startTime")) && StringUtils.isEmpty(params.get("endTime")))) {
			params = new HashMap<String, String>();
			params.put("defaultTime","1");
			return ResponseVo.ok(outsideQueryService.getOutStockStocks(params));
		}
		
		//params不为空时检查日期区间
		String chkMsg = this.checkDuration(params);
		if(!("0".equals(chkMsg))) {
			return ResponseVo.error(chkMsg);
		}
		
		List<OutsideQuery> list = outsideQueryService.getOutStockStocks(params);

		return ResponseVo.ok(list);


	}
	
	
	private String checkDuration(Map<String,String> params) throws ParseException{	
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");

		if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
			if(StringUtils.isEmpty(startTime)) {
				return "开始时间 startTime 不能为空";
			}
			if(StringUtils.isEmpty(endTime)) {
				return "结束时间 endTime 不能为空";
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = format.parse(startTime);
		Date end = format.parse(endTime);
		long time = end.getTime() - start.getTime();

		if (time > (1000L * 60 * 60 * 24 * 31)) {

			return "开始和结束时间间隔不能大于31天";
		}
		return "0";
	}

	
}

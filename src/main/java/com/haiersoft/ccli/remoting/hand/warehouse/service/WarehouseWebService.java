package com.haiersoft.ccli.remoting.hand.warehouse.service;

import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.haiersoft.ccli.base.entity.Warehouse;
import com.haiersoft.ccli.base.service.WarehouseService;
import com.haiersoft.ccli.common.utils.Result;

/**
 * 
 * @author Connor.M
 * @ClassName: WarehouseWebService
 * @Description: 仓库WebService
 * @date 2016年3月3日 下午3:56:16
 */
@WebService
@Service
public class WarehouseWebService {
	
	@Autowired
	private WarehouseService warehouseService;
	
	/**
	 * 
	 * @author Connor.M
	 * @Description: 获得仓库集合
	 * @date 2016年3月3日 下午4:04:43 
	 * @return
	 * @throws
	 */
	public String getWarehouseList(){
		Result<Warehouse> result = new Result<Warehouse>();
		List<Warehouse> lists = warehouseService.getAll();
		if(null != lists && lists.size() > 0){
			result.setCode(0);
			result.setMsg("查询成功！");
			result.setList(lists);
		}else{
			result.setCode(1);
			result.setMsg("无查询数据！");
		}
		return JSON.toJSONString(result);
	}
}

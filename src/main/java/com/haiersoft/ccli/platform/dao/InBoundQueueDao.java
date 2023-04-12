package com.haiersoft.ccli.platform.dao;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.platform.entity.InBoundQueueVO;
import com.haiersoft.ccli.platform.entity.OutBoundQueueVO;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A0047794
 * @日期 2021/11/29
 * @描述
 */
@Repository
public class InBoundQueueDao extends HibernateDao<InBoundQueueVO, String> {

	public Page<InBoundQueueVO> searchInBondQueue(Page<InBoundQueueVO> page, InBoundQueueVO inBoundQueueVO) {
		StringBuffer sb = new StringBuffer();

		Map<String, Object> parme = new HashMap<>();

		sb.append("select inbound.yyid,inbound.consume_Company consumeCompany, inbound.bill_No billNo,inbound.container_No containerNo,inbound.product_Name productName ,inbound.product_Type productType,inbound.num num,inbound.net_Weight weight,inbound.car_Number carNumber,inbound.is_Check isCheck, " +
				"vehiclecheck.check_Status checkStatus,vehiclecheck.check_Time checkTime,inbound.is_Freetax isFreetax,inbound.is_Zdmt isZdmt,inbound.origin_Country originCountry,inbound.appoint_Date appointDate,");
		sb.append("queue.warehouse_No warehouseNo,queue.platform_No platformNo,queue.platform_Name platformName,queue.auto_Manual_Flag autoManualFlag,queue.queue_Time queueTime,queue.status_Flag statusFlag," +
                "log.start_Time startTime,log.end_Time endTime,log.leave_Time leaveTime");


        sb.append(" from PLATFORM_RESERVATION_INBOUND inbound " +
		        " LEFT JOIN PLATFORM_VEHICLE_CHECK vehiclecheck ON inbound.YYID=vehicleCheck.YYID " +
		        " LEFT JOIN  PLATFORM_QUEUE queue ON inbound.YYID=queue.YYID " +
		        " LEFT JOIN PLATFORM_OPERATION_LOG log ON inbound.YYID=log.YYID " +
		        " and log.DELETED_FLAG = '0' " +
		        "where inbound.DELETED_FLAG = '0' ");

		if((null==inBoundQueueVO.getAppointDateStart() ||"".equals(inBoundQueueVO.getAppointDateStart())) &&
				(null==inBoundQueueVO.getAppointDateEnd() ||"".equals(inBoundQueueVO.getAppointDateEnd()))){


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

			sb.append(" and inbound.appoint_Date >= :appointDataStart ");
			parme.put("appointDataStart", start);

			sb.append(" and inbound.appoint_Date < :appointDataEnd ");
			parme.put("appointDataEnd", end);

		}else {

			if(null!=inBoundQueueVO.getAppointDateStart() && !"".equals(inBoundQueueVO.getAppointDateStart())){
				sb.append(" and inbound.appoint_Date >= :appointDataStart ");
				parme.put("appointDataStart", inBoundQueueVO.getAppointDateStart());
			}

			if(null!=inBoundQueueVO.getAppointDateEnd() && !"".equals(inBoundQueueVO.getAppointDateEnd())){
				sb.append(" and inbound.appoint_Date < :appointDataEnd ");
				parme.put("appointDataEnd", inBoundQueueVO.getAppointDateEnd());
			}
		}

		if(null!=inBoundQueueVO.getCarNumber() && !"".equals(inBoundQueueVO.getCarNumber())){
			sb.append(" and inbound.car_Number like :carNumber ");
			parme.put("carNumber", "%"+inBoundQueueVO.getCarNumber()+"%");
		}

		if(null!=inBoundQueueVO.getContainerNo() && !"".equals(inBoundQueueVO.getContainerNo())){
			sb.append(" and inbound.container_No like :containerNo ");
			parme.put("containerNo", "%"+inBoundQueueVO.getContainerNo()+"%");
		}

		if(null!=inBoundQueueVO.getBillNo() && !"".equals(inBoundQueueVO.getBillNo())){
			sb.append(" and inbound.bill_No like :billNo ");
			parme.put("billNo", "%"+inBoundQueueVO.getBillNo()+"%");
		}

		if(null!=inBoundQueueVO.getStatusFlag() && !"".equals(inBoundQueueVO.getStatusFlag())){
			if("0".equals(inBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag = '0' ");

			}

			if("1".equals(inBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag in('1','2') ");

			}
			if("2".equals(inBoundQueueVO.getStatusFlag())){

				sb.append(" and queue.status_Flag in('3','4','5') ");

			}
		}


		sb.append(" order by queue.status_Flag asc,queue.queue_Time asc");

		//查询对象属性转换
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("consumeCompany", String.class);
		parm.put("billNo", String.class);
		parm.put("containerNo", String.class);
		parm.put("productName", String.class);
		parm.put("productType", String.class);
		parm.put("num", String.class);
		parm.put("weight", String.class);
		parm.put("carNumber", String.class);
		parm.put("originCountry", String.class);
		parm.put("appointDate", Date.class);
		parm.put("warehouseNo", String.class);
		//parm.put("roomNum", String.class);
		//parm.put("locationNo", String.class);
		parm.put("platformNo", String.class);
		parm.put("platformName", String.class);
		parm.put("autoManualFlag", String.class);
		parm.put("queueTime", Date.class);
		parm.put("statusFlag", String.class);
		parm.put("startTime", Date.class);
		parm.put("endTime", Date.class);
		parm.put("leaveTime", Date.class);


		parm.put("isCheck", String.class);
		parm.put("checkStatus", String.class);
		parm.put("isFreetax", String.class);
		parm.put("isZdmt", String.class);
		parm.put("checkTime", Date.class);
		parm.put("yyid", String.class);

		//isCheck  checkStatus checkTime isFreetax isZdmt

	//	Map<String, Object> parm= FrameworkUtil.getPropertiesTypeMap(InBoundQueueVO.class);


		return findPageSql(page, sb.toString(), parm, parme);

		//return findPageSql(page, sb.toString(), queryParams);
	}


}

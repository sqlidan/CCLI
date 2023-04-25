package com.haiersoft.ccli.cost.dao;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.persistence.Page;
import com.haiersoft.ccli.common.utils.StringUtils;
import com.haiersoft.ccli.cost.entity.WorkReport;
/**
 * 
 * @author pyl
 * @ClassName: PieceworkRuleDao
 * @Description: 作业计件DAO
 */
@Repository
public class WorkReportDao extends HibernateDao<WorkReport, Integer>{

	public Page<WorkReport> searchWorkReport(Page<WorkReport> page,WorkReport workReport) {
        Map<String, Object> params = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        //工作类型：1入库  2出库
        String inOutType=(StringUtils.isNull(workReport.getType()))?"":workReport.getType();
        
        StringBuffer sql = new StringBuffer();
        sql.append(""
                + " select re.man as man, re.personTypeNm as personType,"
                + " re.ruleJobType as ruleJobType, sum(re.workload) as workload from (");
        //入库
         if("".equals(inOutType)||"1".equals(inOutType)){
		     sql.append(" select wor.worker_typeid as personType,  " //--人员类型id
				            + " wor.worker_ypename as personTypeNm, "  //人员类型
				            + " wor.worker_typeid as personTypeid, "  //人员类型
				            + " wor.worker_name as man,  " //作业人员
				            + " tr.real_gross_weight*per.ratio*r1.ratio/1000 as workload,  " //计件重量
				 	      + " wor.ratio as personRatio, " //人员系数
				 	      + " a.rule_job_type as ruleJobType," //作业类型
				 	      + " NVL(r1.ratio,0)  as workRatio, " //作业类型系数
				 	      + " tr.real_gross_weight as realGrossWeight "); //实际重量
	           sql.append(" from (");
	           sql.append("       select * from bis_asn  "
	           	                     + " where (asn_state = '3' or asn_state = '4')  "); 
	           if(workReport.getWorkTimeS()!=null){
	                	sql.append(" and to_char(inbound_date,'yyyy-MM-dd') >= :times");
	                	params.put("times", sdf.format(workReport.getWorkTimeS()));
	            }
	            if(workReport.getWorkTimeE()!=null){
	                	sql.append(" and to_char(inbound_date,'yyyy-MM-dd') < :timee");
	                	params.put("timee", sdf.format(workReport.getWorkTimeE()));
	            }	            
		     //sql.append(" and real_gross_weight is not null " );
		     sql.append(" ) a inner join " );
		     sql.append(" (select asn,sum((k.original_piece-k.remove_piece)*k.NET_SINGLE) real_gross_weight from bis_tray_info k where k.CARGO_STATE<>'99' group by k.asn) tr on tr.asn=a.asn " );
              sql.append("  inner join bis_asn_worker wor on wor.link_id=a.asn " );//入库作业表
              sql.append("  inner join base_persontype_rule per on per.person_typeid=wor.worker_typeid " );  
		     sql.append("    left join base_piecework_rule r1 on a.rule_job_type=r1.job_type " );
	  }
        //出库
	 if("".equals(inOutType)||"2".equals(inOutType)){
	      if("".equals(inOutType)){
	          sql.append(" union all ");
	       }
		sql.append(" select lwor.worker_typeid as personType,  " //--人员类型id
			            + " lwor.worker_ypename as personTypeNm, "  //人员类型
			            + " lwor.worker_typeid as personTypeid, "  //人员类型
			            + " lwor.worker_name as man,  " //作业人员
			            + " lod.real_gross_weight*perr.ratio*r5.ratio/1000 as workload,  " //计件重量
			 	      + " lwor.ratio as personRatio, " //人员系数
			 	      + " ord.rule_job_type as ruleJobType, " //作业类型
			 	      + " NVL(r5.ratio,0)  as workRatio, " //作业类型系数
			 	      + " lod.real_gross_weight as realGrossWeight  "); //实际重量
		   sql.append(" from (");
		   sql.append("       select e.loading_plan_num,sum(e.net_weight) as real_gross_weight  ");
		   sql.append("          from bis_loading_info e  ");	      
		   sql.append("         where (e.loading_state = '2' or e.loading_state = '6') ");             
		   if(workReport.getWorkTimeS()!=null){
		        	sql.append(" and to_char(e.loading_time,'yyyy-MM-dd') >= :times");
		        	params.put("times", sdf.format(workReport.getWorkTimeS()));
		    }
		    if(workReport.getWorkTimeE()!=null){
		        	sql.append(" and to_char(e.loading_time,'yyyy-MM-dd') < :timee");
		        	params.put("timee", sdf.format(workReport.getWorkTimeE()));
		    }	            
		 sql.append(" group by e.loading_plan_num " );
		 sql.append(" )lod  " );
		 sql.append("  inner join bis_loading_order ord  on ord.order_num=lod.loading_plan_num " );
		 sql.append("   inner join bis_loading_order_worker lwor on lwor.link_id= ord.order_num " );//出库作业表
		 sql.append("   inner join base_persontype_rule perr on perr.person_typeid=lwor.worker_typeid " );
		 sql.append("   left join base_piecework_rule r5 on ord.rule_job_type=r5.job_type " );//计件规则基础表
	 }
        sql.append(" ) re where   re.realGrossWeight>0 "); 
        if(!StringUtils.isNull(workReport.getPersonName())){
        	sql.append(" and re.man=:name ");
        	params.put("name", workReport.getPersonName());
        }
        if(!StringUtils.isNull(workReport.getPersonType())){
        	sql.append(" and re.personTypeid=:personType ");
        	params.put("personType", workReport.getPersonType());
        }
        sql.append(" group by re.personTypeNm,re.man,ruleJobType ");
        sql.append(" order by re.man,re.personTypeNm,ruleJobType ");
        //查询对象属性转换
        Map<String, Object> parm = new HashMap<String, Object>();
        parm.put("man", String.class);
        parm.put("personType", String.class);
        parm.put("workload", Double.class);
        parm.put("ruleJobType",String.class);
        //parm.put("ratio", Double.class);
        //parm.put("realGrossWeight", Double.class);

        return findPageSql(page, sql.toString(), parm, params);
	}
	
	
	/**
	 * 查询杂项计件统计报表
	 * @param page
	 * @param workReport
	 * @return
	 */
	public Page<WorkReport> searchOtherWorkerReport(Page<WorkReport> page,
			WorkReport workReport) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.worker_name as personName,t.WORKER_YPENAME as jobType,sum(work_duration) as workload ");
		sql.append(" from bis_other_worker t where 1=1 ");
		
		if(!StringUtils.isNull(workReport.getPersonName())){
			sql.append(" and t.worker_name=:personName ");
			params.put("personName", workReport.getPersonName());
		}
		if(!StringUtils.isNull(workReport.getJobType())){
			sql.append(" and t.worker_ypename=:jobType ");
			params.put("jobType", workReport.getJobType());
		}
		if(workReport.getWorkTimeS()!=null){
			sql.append(" and to_char(t.work_date,'yyyy-mm-dd')>=:workTimeS ");
			params.put("workTimeS", sdf.format(workReport.getWorkTimeS()));
		}
		if(workReport.getWorkTimeE()!=null){
			sql.append("and to_char(t.work_date,'yyyy-mm-dd')<=:workTimeE ");
			params.put("workTimeE", sdf.format(workReport.getWorkTimeE()));
		}
		
		sql.append(" group by t.worker_name,t.worker_ypename ");
		
		
		//查询对象属性转换
	    Map<String, Object> parm = new HashMap<String, Object>();
	    parm.put("personName", String.class);
	    parm.put("jobType", String.class);
	    parm.put("workload", Double.class);
	    //parm.put("ruleJobType",String.class);
	    //parm.put("ratio", Double.class);
	    //parm.put("realGrossWeight", Double.class);
	
	    return findPageSql(page, sql.toString(), parm, params);
		
	}


	public Page<WorkReport> searchOtherWorkerReportHeji(Page<WorkReport> page,
			WorkReport workReport) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(work_duration) as workload ");
		sql.append(" from bis_other_worker t where 1=1 ");
		
		if(!StringUtils.isNull(workReport.getPersonName())){
			sql.append(" and t.worker_name=:personName ");
			params.put("personName", workReport.getPersonName());
		}
		if(!StringUtils.isNull(workReport.getJobType())){
			sql.append(" and t.worker_ypename=:jobType ");
			params.put("jobType", workReport.getJobType());
		}
		if(workReport.getWorkTimeS()!=null){
			sql.append(" and to_char(t.work_date,'yyyy-mm-dd')>=:workTimeS ");
			params.put("workTimeS", sdf.format(workReport.getWorkTimeS()));
		}
		if(workReport.getWorkTimeE()!=null){
			sql.append("and to_char(t.work_date,'yyyy-mm-dd')<=:workTimeE ");
			params.put("workTimeE", sdf.format(workReport.getWorkTimeE()));
		}
		
		//sql.append(" group by t.worker_name,t.worker_ypename ");
		
		
		//查询对象属性转换
	    Map<String, Object> parm = new HashMap<String, Object>();
	   // parm.put("personName", String.class);
	   // parm.put("jobType", String.class);
	    parm.put("workload", Double.class);
	  
	
	    return findPageSql(page, sql.toString(), parm, params);
	}
	
	
}

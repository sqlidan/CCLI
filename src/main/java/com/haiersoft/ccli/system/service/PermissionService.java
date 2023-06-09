package com.haiersoft.ccli.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haiersoft.ccli.common.persistence.HibernateDao;
import com.haiersoft.ccli.common.service.BaseService;
import com.haiersoft.ccli.system.dao.PermissionDao;
import com.haiersoft.ccli.system.entity.Permission;

/**
 * 权限service
 * @author ty
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly=true)
public class PermissionService extends BaseService<Permission, Integer>{
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Override
	public HibernateDao<Permission, Integer> getEntityDao() {
		return permissionDao;
	}
	
	/**
	 * 添加菜单基础操作
	 * @param pid 菜单id
	 * @param pName 菜单权限标识名
	 */
	@Transactional(readOnly = false)
	public void addBaseOpe(Integer pid,String pClassName){
		List<Permission> pList=new ArrayList<Permission>();
		pList.add(new Permission(pid, "添加", "O", "", "sys:"+pClassName+":add"));
		pList.add(new Permission(pid, "删除", "O", "", "sys:"+pClassName+":delete"));
		pList.add(new Permission(pid, "修改", "O", "", "sys:"+pClassName+":update"));
		pList.add(new Permission(pid, "查看", "O", "", "sys:"+pClassName+":view"));
		
		//添加没有的基本操作
		List<Permission> existPList=getMenuOperation(pid);
		for(Permission permission:pList){
			boolean exist=false;
			for(Permission existPermission:existPList){
				if(permission.getPermCode().equals(existPermission.getPermCode())){
					exist=true;
					break;
				}else{
					exist=false;
				}
			}
			if(!exist)
				save(permission);
		}
	}
	
	/**
	 * 获取角色拥有的权限集合
	 * @param userId
	 * @return 结果集合
	 */
	public List<Permission> getPermissions(Integer userId){
		return permissionDao.findPermissions(userId);
	}
	
	/**
	 * 获取角色拥有的权限集合
	 * @param userId
	 * @return 结果集合
	 */
	public List<Permission> getPermissionsCW(Integer userId){
		return permissionDao.findPermissionsCW(userId);
	}
	/**
	 * 获取角色拥有的菜单
	 * @param userId
	 * @return 菜单集合
	 */
	public List<Permission> getMenus(Integer userId){
		return permissionDao.findMenus(userId);
	}
	
	/**
	 * 获取所有菜单
	 * @return 菜单集合
	 */
	public List<Permission> getMenus(){
		return permissionDao.findMenus();
	}
	
	/**
	 * 获取菜单下的操作
	 * @param pid
	 * @return 操作集合
	 */
	public List<Permission> getMenuOperation(Integer pid){
		return permissionDao.findMenuOperation(pid);
	}
	
	/**
	 * 获取角色的手持权限
	 * @param userId
	 * @return 菜单集合
	 */
	public List<Permission> findHand(String loginName){
		return permissionDao.findHand(loginName);
	}

	//删除权限角色表
	public void deleterp(Integer id) {
		permissionDao.deleterp(id);
	}

	/**
	 * 判断当前用户是否有展期权限
	 * @param userId
	 * @return 结果集合
	 */
	public List<Permission> ifzq(Integer userId){
		return permissionDao.ifzq(userId);
	}
}

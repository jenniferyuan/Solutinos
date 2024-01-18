package com.yuan.demo;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yuan.demo.entity.manytomany.Menus;
import com.yuan.demo.entity.manytomany.Roles;
import com.yuan.demo.repository.RolesDao;

@SpringBootTest
class BootJpaManyToManyTest {
	
	@Autowired
	private RolesDao rolesDao;
	
	/**
	 * 添加角色同时添加菜单
	 */
	@Test
	public void test1(){
		//创建角色对象
		Roles roles = new Roles();
		roles.setRolename("SuperAdmin");
		
		//创建菜单对象    XXX管理平台 --->用户管理
		Menus menus = new Menus();
		menus.setMenusname("XXXManage System");
		menus.setFatherid(-1);
		menus.setMenusurl(null);
		
		//用户管理菜单
		Menus menus1 = new Menus();
		menus1.setMenusname("UserManager");
		menus1.setFatherid(1);
		menus1.setMenusurl(null);
		
		
		//建立关系
		roles.getMenus().add(menus);
		roles.getMenus().add(menus1);
		
		menus.getRoles().add(roles);
		menus1.getRoles().add(roles);
		
		
		//保存数据
		this.rolesDao.save(roles);
	}
	
	/**
	 * 查询Roles
	 */
	@Test
	public void test2(){
		Roles roles = this.rolesDao.findById(1).get();
		System.out.println("角色信息："+roles);
		Set<Menus> menus = roles.getMenus();
		for (Menus menus2 : menus) {
			System.out.println("菜单信息："+menus2);
		}
	}
}

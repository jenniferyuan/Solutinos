package com.yuan.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.yuan.demo.entity.manytomany.Roles;

/**
 * 多对多关联关系讲解
 * @author Administrator
 */
public interface RolesDao extends JpaRepository<Roles, Integer> {

}

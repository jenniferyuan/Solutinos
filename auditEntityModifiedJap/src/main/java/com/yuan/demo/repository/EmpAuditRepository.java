package com.yuan.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuan.demo.entity.EmpAudit;

@Repository
public interface EmpAuditRepository extends JpaRepository<EmpAudit, Long> {

}

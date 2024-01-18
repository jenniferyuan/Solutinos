package com.yuan.demo.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity(name = "EmpAudit")
@Table(name = "emp_audit", schema = "test")
public class EmpAudit implements Serializable {

	private static final long serialVersionUID = -296627590880006857L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_AUDIT_ID", unique = true, nullable = false)
	private Long empAuditId;
	@Column(name = "TABLE_NAME")
	private String tableName;
	@Column(name = "PKEY_FIELD_VALUE")
	private long pkeyFieldValue;
	@Column(name = "FIELD_NAME")
	private String fieldName;
	@Column(name = "OLD_VALUE")
	private String oldValue;
	@Column(name = "NEW_VALUE")
	private String newValue;
	@Column(name = "INSERTED_BY", nullable = false)
	private String insertedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTED_DT", nullable = false)
	private Date insertedDt;
	public Long getEmpAuditId() {
		return empAuditId;
	}
	public void setEmpAuditId(Long empAuditId) {
		this.empAuditId = empAuditId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getPkeyFieldValue() {
		return pkeyFieldValue;
	}
	public void setPkeyFieldValue(long pkeyFieldValue) {
		this.pkeyFieldValue = pkeyFieldValue;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getInsertedBy() {
		return insertedBy;
	}
	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}
	public Date getInsertedDt() {
		return insertedDt;
	}
	public void setInsertedDt(Date insertedDt) {
		this.insertedDt = insertedDt;
	}
}
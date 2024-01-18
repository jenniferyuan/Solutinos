package com.yuan.demo.entity.manytomany;

import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "Menus")
@Table(name = "t_menus", schema="sql5661817")
public class Menus {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="menusid")
	private Integer menusid;
	
	@Column(name="menusname")
	private String menusname;
	
	@Column(name="menusurl")
	private String menusurl;
	
	@Column(name="fatherid")
	private Integer fatherid;

	// mappedBy名稱為另一側有@ManyToMany的屬性名稱
	@ManyToMany(mappedBy="menus", fetch=FetchType.EAGER)
	private Set<Roles> roles = new HashSet<>();
	
	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Integer getMenusid() {
		return menusid;
	}

	public void setMenusid(Integer menusid) {
		this.menusid = menusid;
	}

	public String getMenusname() {
		return menusname;
	}

	public void setMenusname(String menusname) {
		this.menusname = menusname;
	}

	public String getMenusurl() {
		return menusurl;
	}

	public void setMenusurl(String menusurl) {
		this.menusurl = menusurl;
	}

	public Integer getFatherid() {
		return fatherid;
	}

	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

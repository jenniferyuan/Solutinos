package com.yuan.demo.entity;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Proxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Article") // 设置ORM实体类名稱(JPQL使用名稱)
@Table(name = "jpa_article", schema="test") // 指定映射的表名
public class Article {
	
	@Id // 表明映射对应的主键id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 设置主键自增策略
	private Integer id;
	@Column(name="title")
	private String title;
	@Column(name="content")
	private String content;
	@Transient	// 不屬於Entity中的屬性
	private String tempValue;

	// 一對多
	// mappedBy的name是指有多的一端含有@ManyToOne的屬性名稱
	// 不使用懶加載FetchType.LAZY否則報org.hibernate.LazyInitializationException: 
	// failed to lazily initialize a collection of role
	@OneToMany(mappedBy="article", fetch=FetchType.EAGER) 
	//@JoinColumn(name="aid", referencedColumnName = "id")
	private List<Comment> commentList = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

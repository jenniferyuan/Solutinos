package com.yuan.demo.entity;

import java.util.Date;


import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity(name="Comment")	// 设置ORM实体类名稱(JPQL使用名稱)
@Table(name="jpa_comment", schema="test") // 指定映射的表名
public class Comment {

	@Id // 表明映射对应的主键id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 设置主键自增策略
	private Integer id;
	@Column(name = "article_id") // 指定映射的表字段名
	private Integer articleId;
	@Column(name = "content")
	private String content;
	@Column(name = "author")
	private String author;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "inserted_dt")
	private Date insertedDt;
	@Transient	// 不屬於Entity中的屬性
	private String tempValue;

	// 多對一
	@ManyToOne(cascade = CascadeType.PERSIST) // 新增Comment同時新增Article
	@JoinColumn(name="fr_article_id")
	private Article article;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getInsertedDt() {
		return insertedDt;
	}

	public void setInsertedDt(Date insertedDt) {
		this.insertedDt = insertedDt;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
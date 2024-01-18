package com.yuan.demo.vo;

import java.util.Date;

import com.yuan.demo.custom.ColumnConf;

public class CommentVo {
	
	private Integer id;
	@ColumnConf("作者id")
	private Integer articleId;
	@ColumnConf("內容")
	private String content;
	@ColumnConf("作者姓名")
	private String author;
	@ColumnConf("日期")
	private Date insertedDt;
	
	public CommentVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommentVo(Integer id, Integer articleId, String content, String author, Date insertedDt) {
		super();
		this.id = id;
		this.articleId = articleId;
		this.content = content;
		this.author = author;
		this.insertedDt = insertedDt;
	}

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
}

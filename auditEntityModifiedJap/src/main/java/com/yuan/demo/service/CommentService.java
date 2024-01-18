package com.yuan.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuan.demo.entity.Comment;
import com.yuan.demo.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	@Transactional
	public Comment updateAndSave(Comment comment) {
		comment.setArticleId(1);
		comment.setAuthor("Brian33");
		comment.setContent("content by Brian33");
		comment.setInsertedDt(new Date());
		return commentRepository.save(comment);
	}
}

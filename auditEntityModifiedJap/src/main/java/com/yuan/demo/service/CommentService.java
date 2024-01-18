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
		comment.setArticleId(2);
		comment.setAuthor("Brian21");
		comment.setContent("content by Brian21");
		comment.setInsertedDt(new Date());
		return commentRepository.save(comment);
	}
}

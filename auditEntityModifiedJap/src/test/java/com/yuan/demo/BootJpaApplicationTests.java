package com.yuan.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yuan.demo.entity.Comment;
import com.yuan.demo.repository.CommentRepository;
import com.yuan.demo.service.CommentService;

@SpringBootTest
class BootJpaApplicationTests {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Test // Hibernate Interceptor onFlush 及 Aspect AOP 測試
	public void saveUpdateComment() {
		Comment comment = commentRepository.findById(1).get();
		Comment entity = commentService.updateAndSave(comment);
		System.out.println(entity);
	}
}

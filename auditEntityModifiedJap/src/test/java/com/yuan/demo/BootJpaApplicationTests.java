package com.yuan.demo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.yuan.demo.entity.Article;
import com.yuan.demo.entity.Comment;
import com.yuan.demo.repository.CommentRepository;
import com.yuan.demo.service.CommentService;

@SpringBootTest
class BootJpaApplicationTests {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Test //onLoad測試 
    public void findAll() {
		List<Comment> list = commentRepository.findAll();
		System.out.println("list=>" + list);
	}
	
	@Test 
	public void findById() {
		Optional<Comment> comment = commentRepository.findById(1);
		System.out.println(comment.get());
	}
	
	@Test 
	public void findByAuthorNotNull() {
		List<Comment> list = commentRepository.findByAuthorNotNull();
		System.out.println(list);
	}

	@Test
	public void getCommentPage() {
		//展示第一頁的3筆數據
		Pageable pageable = PageRequest.of(0, 3);
		List<Comment> commentList = commentRepository.getCommentPaged(1, pageable);
		System.out.println(commentList);
	}
	
	@Test
	public void getCommentPagedNativeSql() {
		//展示第一頁的3筆數據
		Pageable pageable = PageRequest.of(0, 3);
		List<Comment> commentList = commentRepository.getCommentPagedNativeSql(1, pageable);
		System.out.println(commentList);
	}
	
	// 透過SQL或JPQL執行update前不會執行Interceptor onFlushDirty
	@Test
	public void updateComment() {
		int row = commentRepository.updateComment("JenniferYuan", 5);
		System.out.println(row);
	}
	
	@Test
	public void deleteComment() {
		int row = commentRepository.deleteComment(15);
		System.out.println(row);
	}
	
	@Test // interceptor onSave測試
	public void saveComment() {
		Comment comment = new Comment();
		comment.setArticleId(2);
		comment.setAuthor("Wong24");
		comment.setContent("text555");
		comment.setInsertedDt(new Date());
		Comment entity = commentRepository.save(comment);
		System.out.println(entity);
	}
	
	@Test // interceptor onFlush測試
	public void saveUpdateComment() {
		Comment comment = commentRepository.findById(1).get();
		comment.setArticleId(1);
		comment.setAuthor("Brian22");
		comment.setContent("content by Brian22");
		comment.setInsertedDt(new Date());
		Comment entity = commentRepository.save(comment);
		System.out.println(entity);
	}
	
	@Test // interceptor onFlush測試
	public void saveUpdateComment2() {
		Comment comment = commentRepository.findById(1).get();
		Comment entity = commentService.updateAndSave(comment);
		System.out.println(entity);
	}
	
	@Test
	public void manyToOneSaveTest() {
		//創建comment
		Comment comment = new Comment();
		comment.setArticleId(1);
		comment.setAuthor("Queen2");
		comment.setContent("Queen225");
		comment.setInsertedDt(new Date());
		//創建一個Article
		Article article = new Article();
		article.setTitle("cascade2");
		article.setContent("Queen225");
		//關聯
		article.getCommentList().add(comment);
		comment.setArticle(article);
		//保存
		Comment entity = commentRepository.save(comment);
		System.out.println(entity);
	}
	
	@Test
	public void manyToOneFindTest() {
		Optional<Comment> comment = commentRepository.findById(5);
		System.out.println(comment.get());
		System.out.println(comment.get().getArticle().toString());
	}
}

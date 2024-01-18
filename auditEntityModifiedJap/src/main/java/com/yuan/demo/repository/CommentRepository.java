package com.yuan.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yuan.demo.entity.Comment;
import com.yuan.demo.vo.CommentVo;

//返回的實體類名稱及主鍵類型
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	List<Comment> findByAuthorNotNull();
	
	//	@Query表示增、刪、改、查的操作
	
	//	save為JpaRepository定義直接調用即可
	
	@Query("SELECT c FROM Comment c WHERE c.articleId = ?1") //1表示第一個參數
    List<Comment> getCommentPaged(Integer articleid, Pageable pageable);
	
	@Query(value = "SELECT * FROM jpa_comment WHERE article_id = ?1", nativeQuery = true)
    List<Comment> getCommentPagedNativeSql(Integer article_id, Pageable pageable);
	
	@Transactional // 表示支持事务管理
    @Modifying 	   // 表示支持数据变更(修改、刪除)
    @Query("UPDATE Comment c SET c.author = :author WHERE c.id = :id")
    Integer updateComment(@Param("author")String author, @Param("id")Integer id);
	
	@Transactional
    @Modifying
    @Query("DELETE Comment c WHERE c.id = ?1")
    Integer deleteComment(Integer id);
	
	
	// Select straight to VO(用於多表join查詢)
	// 優點：不需再做 entity 轉 vo
	// Ref:YlPartsModelRepository, YlPmPlanSetDetailRepository
	@Query("SELECT new com.yuan.demo.vo.CommentVo("
			+ "c.id, "
			+ "c.articleId, "
			+ "c.content, "
			+ "c.author, "
			+ "c.insertedDt) "
			+ "FROM Comment c"
		  )
	List<CommentVo> findAll2Vo();
	
	// Specification
	Comment findOne(Specification<Comment> spec);
	List<Comment> findAll(Specification<Comment> spec);
	List<Comment> findAll(Specification<Comment> spec, Sort sort);
	Page<Comment> findAll(Specification<Comment> spec, Pageable pageable);
	
}

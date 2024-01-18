package com.yuan.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuan.demo.entity.Comment;

//返回的實體類名稱及主鍵類型
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	
}

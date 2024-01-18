package com.yuan.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuan.demo.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}

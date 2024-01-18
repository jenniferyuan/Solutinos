package com.yuan.demo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.yuan.demo.entity.Comment;
import com.yuan.demo.repository.CommentRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

//https://blog.csdn.net/cold___play/article/details/103578641
//JPA--SpringDataJpa之Specifications动态查询

@SpringBootTest
class BootJpaCriteriaApplicationTests {
	
	@Autowired
	private CommentRepository commentRepository;
	
	/*
	   	root：获取属性（客户名，所属行业）
	    criteriaBuilder：
	        1.构造客户名的精准匹配查询
	        2.构造所属行业的精准匹配查询
	        3.将以上两个查询联系起来
	*/

	// 单个条件查询单个对象
	@Test 
	public void SpecificationTest1() {
		Specification<Comment> spec = new Specification<Comment>() {
			private static final long serialVersionUID = 1L;
			@Override
	        public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	            //获取属性
	            Path<Object> author = root.get("author");
	            //构造查询条件 select * from cst_customer where cust_name = 'EXPRESS'
	            Predicate predicate = cb.equal(author, "EXPRESS");//精准匹配
	            return predicate;
	        }
	    };
	    Comment comment = commentRepository.findOne(spec);
		System.out.println("comment=>" + comment);
	}
	
	// 多条件查询单个对象
	@Test 
	public void SpecificationTest2() {
		Specification<Comment> spec = new Specification<Comment>() {
            private static final long serialVersionUID = 1L;
			@Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> content = root.get("content");//客户名
                Path<Object> author = root.get("author");//所属行业
 
                //构造查询
                //1.构造content的精准匹配查询
                Predicate p1 = cb.equal(content, "content2");
                //2..构造author的精准匹配查询
                Predicate p2 = cb.equal(author, "Yuan2");
                //3.将多个查询条件组合到一起：组合（满足条件一并且满足条件二：与关系，满足条件一或满足条件二即可：或关系）
                Predicate and = cb.and(p1, p2);//以与的形式拼接多个查询条件
                // cb.or();//以或的形式拼接多个查询条件
                return and;
            }
        };
        Comment comment = commentRepository.findOne(spec);
        System.out.println("comment=>" + comment);
	}
	
	
	//  排序查询
	/**
	 *
     * equal ：直接的到path对象（属性），然后进行比较即可
     * gt，lt,ge,le,like : 得到path对象，根据path指定比较的参数类型，再去进行比较
     *      指定参数类型：path.as(类型的字节码对象)
     */
	@Test 
	public void SpecificationTest3() {
		Specification<Comment> spec = new Specification<Comment>() {
            private static final long serialVersionUID = 1L;
			@Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查询属性：客户名
                Path<Object> author = root.get("author");
                //查询方式：模糊匹配
                Predicate like = cb.like(author.as(String.class), "Hsu%");
                return like;
            }
        };
        List<Comment> list = commentRepository.findAll(spec);
        for (Comment comment : list) {
            System.out.println(comment);
        }
        //添加排序
        Sort sort = Sort.by("articleId").ascending();
        List<Comment> listOrder = commentRepository.findAll(spec, sort);
        for (Comment comment : listOrder) {
            System.out.println(comment);
        }
	}

	
	/*
	    1.findAll(Specification, Pageable)：有条件的分页
	    2.findALL(Pageable)：没有条件的分页
	        Specification：查询条件
	        Pageable：分页参数
	        返回：Page（springdataJpa为我们封装好的pageBean对象）
	 */
	// 分页查询
	@Test
	public void SpecificationTest4() {
		Specification<Comment> spec = new Specification<Comment>() {
            private static final long serialVersionUID = 1L;
			@Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查询属性：客户名
                Path<Object> author = root.get("author");
                //查询方式：模糊匹配
                Predicate predicate = cb.notEqual(author, "");
                return predicate;
            }
        };
	    /*
	        PageRequest是Pageable的实现类，在创建pageable的过程中，需要调用它的构造方法传入两个参数
	            1.当前查询的页数（从0开始）
	            2.每页查询的数量
	     */
		Sort sort = Sort.by("articleId");
	    Pageable pageable = PageRequest.of(0, 2, sort);
	    Page<Comment> page = commentRepository.findAll(spec ,pageable);
	    /*
	        Page方法：
	            getContent():获取查询的结果，返回List<?>
	            getTotalElements():获取查询到的总条数，返回Long
	            getTotalPages()：获取查询到的总页数，返回int
	     */
	    System.out.println("数据：" + page.getContent());
	    System.out.println("总条数：" + page.getTotalElements());
	    System.out.println("总页数" + page.getTotalPages());
	}
}

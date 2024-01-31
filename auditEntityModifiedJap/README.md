
## 基於SpringBoot開發記錄Entity修改前後數據
```text
業界開發中通常會有此需求，需將數據的變化寫進資料庫中，這裡使用兩種方式
解決方案1. 使用 Hibernate Interceptor
解決方案2. 使用 AOP
```
### 開發環境:
```text
Springboot 3.1.5
Maven 3.6.3
Java jdk 17
MySql 8
```
### 方案1.使用 Hibernate Interceptor(限JPA)
step1. 新增一個類實現 org.hibernate.Interceptor，並重寫 onFlushDirty 方法。
```java
public class JpaInterceptor implements Interceptor {

	@Override
	public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
			
			...................
	}
}
```
step2. 因為上面的攔截器只是一個普通Java類，所以需要新增一個類實現 org.springframework.context.ApplicationContextAware，
       通過該類來注入IOC容器中的對象。
```java
@Component
public class SpringContextService implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringContextService.applicationContext == null) {
			SpringContextService.applicationContext = applicationContext;
		}
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}
```
step3.配置文件中配置。
```yml
spring:
  jpa:
    properties:
      hibernate:
        hbm2ddl: 
        '[session_factory]':
          interceptor: com.yuan.demo.interceptor.JpaInterceptor
```

### 方案2.使用 AOP(任何ORM都適用)
step1. 新增一個Aspect類實現。
```java
@Aspect
@Component
public class EntityUpdateAspect {
	
	@Before("execution(* com.yuan.demo.service.CommentService.updateAndSave(..)) && args(entity)")
    public void logPreUpdate(JoinPoint joinPoint, Object entity) {
		.......................................
	}
	
	@AfterReturning(value = "execution(* com.yuan.demo.service.CommentService.updateAndSave(..))", returning = "result")
    public void logPostUpdate(JoinPoint joinPoint, Object result) {
    	........................................
	}
}
```

##  範例程式使用
```text
本範例已豬使用的entity已有sql腳本，只要將application.yml配置檔中的 sql mode 設定為always即可
```
測試:
```java
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
```
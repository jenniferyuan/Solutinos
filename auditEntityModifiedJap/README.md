
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
step1. 實現一個 org.hibernate.Interceptor類，並重寫 onFlushDirty 方法
```java
public class JpaInterceptor implements Interceptor {

	@Override
	public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
			
			...................
	}
}
```

### 方案2.使用 AOP(任何ORM都適用)

##  範例程式使用
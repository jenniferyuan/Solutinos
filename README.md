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
### 方案1.使用 Hibernate Interceptor
### 方案2.使用 AOP
package com.yuan.demo.aop;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuan.demo.entity.EmpAudit;
import com.yuan.demo.repository.EmpAuditRepository;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Aspect
@Component
public class EntityUpdateAspect {
	
	@Autowired
	private EmpAuditRepository empAuditRepository;
	
	
	private String tableName = null;
	private Object[] previousState = null;
	private String[] propertyNames = null;
	private long pid;
	
	@Before("execution(* com.yuan.demo.service.CommentService.updateAndSave(..)) && args(entity)")
    public void logPreUpdate(JoinPoint joinPoint, Object entity) {
		System.out.println("=====execute logPreUpdate=====");
		try {
			// 1) TableName
			Class<?> entityClass = entity.getClass();
			Table table = entityClass.getAnnotation(Table.class);
			tableName = table.schema() + "." + table.name();
			//System.out.println("TableName=>"+tableName);
			
			// 2) PrimaryKey
			Field jpaIdField = getJpaIdField(entityClass);
			Object pidObj = jpaIdField.get(entity);
			pid = Long.parseLong(pidObj.toString());
			//System.out.println("PId=>"+pid);
			
			// 3) PreviousObj
			Field[] fields = entityClass.getDeclaredFields();
			previousState = Arrays.stream(fields).map(field->{
				try {
					field.setAccessible(true);
					return field.get(entity);
				} catch (Exception e) {
					return null;
				}
			}).toArray(Object[]::new);
			//Arrays.stream(previousObj).forEach(System.out::println);
			
			// 4) Entity Properties
			propertyNames = Arrays.stream(fields).map(field->field.getName()).toArray(String[]::new);
			//Arrays.stream(propertyNames).forEach(System.out::println);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }

    @AfterReturning(value = "execution(* com.yuan.demo.service.CommentService.updateAndSave(..))", returning = "result")
    public void logPostUpdate(JoinPoint joinPoint, Object result) {
    	System.out.println("=====execute logPostUpdate=====");
    	Class<?> entityClass = result.getClass();
    	Field[] fields = entityClass.getDeclaredFields();
    	Object[] currentState = Arrays.stream(fields).map(field->{
    		try {
    			field.setAccessible(true);
        		return field.get(result);
			} catch (Exception e) {
				return null;
			}
    	}).toArray(Object[]::new);
    	//Arrays.stream(currentObject).forEach(System.out::println);
    	for (int i = 0; i < propertyNames.length; i++) {

			Object currentObj = currentState[i];
			Object previousObj = previousState[i];
			String propertyName = propertyNames[i];

			if (currentObj instanceof Set || currentObj instanceof List)
				continue;

			EmpAudit empAudit = null;
			if (!propertyName.equalsIgnoreCase("updatedDt") && !propertyName.equalsIgnoreCase("updatedBy")
					&& !propertyName.equalsIgnoreCase("insertedDt")
					&& !propertyName.equalsIgnoreCase("insertedBy")) {
				if (isObjValueDiff(currentObj, previousObj)) {
					// System.out.println("propertyName="+propertyName);
					// System.out.println("currentVal="+getObjectValue(currentObj));
					empAudit = new EmpAudit();
					empAudit.setTableName(tableName);
					empAudit.setPkeyFieldValue(pid);
					empAudit.setFieldName(propertyName);
					empAudit.setOldValue(getObjectValue(previousObj));
					empAudit.setNewValue(getObjectValue(currentObj));
					empAudit.setInsertedBy("Aspect AOP");
					empAudit.setInsertedDt(new Date());
					empAuditRepository.save(empAudit);
				}
				currentObj = null;
				previousObj = null;
				propertyName = null;
			}
		}
    }
    
    public static Field getJpaIdField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Field item = null;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				field.setAccessible(true);
				item = field;
				break;
			}
		}
		if (item == null) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				item = getJpaIdField(superclass);
			}
		}
		return item;
	}

	private boolean isObjValueDiff(Object oldObj, Object newObj) {
		boolean bool = false;

		if ((oldObj == null && newObj != null) || (oldObj != null && newObj == null))
			return true;

		// if one of the object is null it should never pass this line.

		if (oldObj instanceof String || oldObj instanceof Character) {
			if (!oldObj.toString().equals(newObj.toString()))
				bool = true;

		} else if (oldObj instanceof Integer) {
			if (((Integer) oldObj).intValue() != ((Integer) newObj).intValue())
				bool = true;
		} else if (oldObj instanceof Long) {
			if (((Long) oldObj).longValue() != ((Long) newObj).longValue())
				bool = true;

		} else if (oldObj instanceof Boolean) {

			if (((Boolean) oldObj).booleanValue() != ((Boolean) newObj).booleanValue())
				bool = true;
		} else if (oldObj instanceof Timestamp) {
			if (newObj instanceof Timestamp) {
				if (((Timestamp) oldObj).compareTo((Timestamp) newObj) != 0)
					bool = true;
			} else {
				if (((Timestamp) oldObj).getTime() != ((Date) newObj).getTime())
					bool = true;
			}
		} else if (oldObj instanceof Date) {
			if (newObj instanceof Date) {
				if (((Date) oldObj).compareTo((Date) newObj) != 0)
					bool = true;
			} else {
				if (((Date) oldObj).getTime() != ((Date) newObj).getTime())
					bool = true;
			}
		} else if (oldObj instanceof LocalDate) {
			if (newObj instanceof LocalDate) {
				if (((LocalDate) oldObj).compareTo((LocalDate) newObj) != 0)
					bool = true;
			} else {
				if (((LocalDate) oldObj).equals((LocalDate) newObj))
					bool = true;
			}
		} else if (oldObj instanceof LocalDateTime) {
			if (newObj instanceof LocalDateTime) {
				if (((LocalDateTime) oldObj).compareTo((LocalDateTime) newObj) != 0)
					bool = true;
			} else {
				if (!((LocalDateTime) oldObj).equals((LocalDateTime) newObj))
					bool = true;
			}
		} else if (oldObj instanceof byte[]) {
			if (newObj instanceof byte[]) {
				bool = !Arrays.equals((byte[]) oldObj, (byte[]) newObj);
			}
		}
		return bool;
	}

	private String getObjectValue(Object obj) {
		if (obj != null) {
			if (obj instanceof String || obj instanceof Character || obj instanceof Integer || obj instanceof Long
					|| obj instanceof Boolean || obj instanceof Timestamp || obj instanceof Date
					|| obj instanceof LocalDateTime || obj instanceof LocalDate)
				return obj.toString();
			else if (obj instanceof byte[]) {
				return Integer.toString(((byte[]) obj).length);
			}
		}
		return null;
	}

}

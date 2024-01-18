package com.yuan.demo.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import com.yuan.demo.entity.EmpAudit;
import com.yuan.demo.repository.EmpAuditRepository;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
public class JpaInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	
	// 查詢數據前執行
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		// TODO Auto-generated method stub
		System.out.println("=====execute onLoad=====");
		return super.onLoad(entity, id, state, propertyNames, types);
	}
	
	// 新增數據前執行
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
			throws CallbackException {
		// TODO Auto-generated method stub
		System.out.println("=====execute onSave=====");
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
	// 數據更新前執行
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		System.out.println("=====execute onFlushDirty=====");
		EmpAuditRepository empAuditRepository = SpringContextService.getBean(EmpAuditRepository.class);
		Class<?> entityClass = null;
		Table table = null;
		Field jpaIdField = null;
		String tableName = null;
		try {
			entityClass = entity.getClass();
			table = entityClass.getAnnotation(Table.class);
			jpaIdField = getJpaIdField(entityClass);
			tableName = table.schema() + "." + table.name();
			System.out.println("TableName=>"+tableName);
			Object pidObj = jpaIdField.get(entity);
			long pid = Long.parseLong(pidObj.toString());
			System.out.println("PId=>"+pid);
//			String updatedBy = null;
//			for (int i = 0; i < propertyNames.length; i++) {
//				if (propertyNames[i].equalsIgnoreCase("updatedBy")) {
//					updatedBy = currentState[i].toString() == null ? "" : currentState[i].toString();
//					System.out.println("updatedBy="+currentState[i]);
//				}
//			}
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
						empAudit.setInsertedBy("JiaYu");
						empAudit.setInsertedDt(new Date());
						empAuditRepository.save(empAudit);
					}
					currentObj = null;
					previousObj = null;
					propertyName = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
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
package com.stockstreaming.demo.service.query;

import com.stockstreaming.demo.model.FilterOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class DynamicSpecification<T> implements Specification<T> {

    private final String field;
    private final FilterOperator operator;
    private final Object value;

    public static <T> DynamicSpecification<T> with(String field, FilterOperator op, Object val) {
        return new DynamicSpecification<>(field, op, val);
    }

    private DynamicSpecification(String field, FilterOperator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Path<?> path = root.get(field);

        switch (operator) {

            case GREATER_THAN:
                return handleGreaterThan(path, cb);

            case LESS_THAN:
                return handleLessThan(path, cb);

            case EQUALS:
                return cb.equal(path, value);

            case CONTAINS:
                return cb.like(cb.lower(path.as(String.class)),
                        "%" + value.toString().toLowerCase() + "%");

            default:
                throw new UnsupportedOperationException(operator + " not supported");
        }
    }

    private Predicate handleGreaterThan(Path<?> path, CriteriaBuilder cb) {
        Class<?> type = path.getJavaType();

        if (Number.class.isAssignableFrom(type)) {
            Number numberValue = convertToNumber(value, (Class<? extends Number>) type);
            return cb.gt(path.as((Class<? extends Number>) type), numberValue);
        }

        if (Comparable.class.isAssignableFrom(type)) {
            Comparable comparableValue = (Comparable) value;
            return cb.greaterThan(path.as(Comparable.class), comparableValue);
        }

        throw new IllegalArgumentException("GREATER_THAN not supported for type " + type.getName());
    }

    private Predicate handleLessThan(Path<?> path, CriteriaBuilder cb) {
        Class<?> type = path.getJavaType();

        if (Number.class.isAssignableFrom(type)) {
            Number numberValue = convertToNumber(value, (Class<? extends Number>) type);
            return cb.lt(path.as((Class<? extends Number>) type), numberValue);
        }

        if (Comparable.class.isAssignableFrom(type)) {
            Comparable comparableValue = (Comparable) value;
            return cb.lessThan(path.as(Comparable.class), comparableValue);
        }

        throw new IllegalArgumentException("LESS_THAN not supported for type " + type.getName());
    }

    private Number convertToNumber(Object val, Class<? extends Number> targetType) {

        if (targetType == Integer.class) {
            return Integer.valueOf(val.toString());
        }
        if (targetType == Long.class) {
            return Long.valueOf(val.toString());
        }
        if (targetType == Double.class) {
            return Double.valueOf(val.toString());
        }
        if (targetType == Float.class) {
            return Float.valueOf(val.toString());
        }
        if (targetType == Short.class) {
            return Short.valueOf(val.toString());
        }
        if (targetType == BigDecimal.class) {
            return new BigDecimal(val.toString());
        }

        throw new IllegalArgumentException("Unsupported numeric type: " + targetType.getName());
    }
}

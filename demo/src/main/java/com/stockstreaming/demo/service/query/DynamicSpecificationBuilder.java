package com.stockstreaming.demo.service.query;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class DynamicSpecificationBuilder<T> {
    @SuppressWarnings("unchecked")
    public Specification<T> build(List<DynamicFilter> filters) {

        return (root, query, cb) -> {

            Predicate finalPredicate = cb.conjunction();

            for (DynamicFilter filter : filters) {
                Expression<?> path = root.get(filter.getField());

                Predicate p = switch (filter.getOperator()) {
                    case EQUAL -> cb.equal(path, filter.getValue());
                    case LIKE -> cb.like(cb.lower(path.as(String.class)),
                            "%" + filter.getValue().toString().toLowerCase() + "%");
                    case GREATER_THAN -> cb.greaterThan(
                            root.get(filter.getField()),
                            (Comparable) filter.getValue()
                    );
                    case LESS_THAN -> cb.lessThan(
                            root.get(filter.getField()),
                            (Comparable) filter.getValue()
                    );
                    case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(
                            root.get(filter.getField()),
                            (Comparable) filter.getValue()
                    );
                    case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(
                            root.get(filter.getField()),
                            (Comparable) filter.getValue()
                    );
                };

                finalPredicate = cb.and(finalPredicate, p);
            }

            return finalPredicate;
        };
    }
}

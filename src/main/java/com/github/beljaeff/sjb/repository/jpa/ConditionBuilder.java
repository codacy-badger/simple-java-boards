package com.github.beljaeff.sjb.repository.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.Collection;

import static org.springframework.util.CollectionUtils.isEmpty;

//TODO: test it
class ConditionBuilder {
    private CriteriaBuilder builder;
    private Predicate result;

    ConditionBuilder(CriteriaBuilder builder) {
        this.builder = builder;
    }

    /**
     * @param joinWithOr true => we use criteriaBuilder.or(), false => we use criteriaBuilder.and()
     * @param useNull true => we use criteriaBuilder.isNull(), false => we use criteriaBuilder.equal()
     */
    <S> ConditionBuilder joinEqual(Path<S> path, S object, boolean joinWithOr, boolean useNull) {
        if(path == null || object == null && !useNull) {
            return this;
        }

        Predicate current = useNull && object == null ? builder.isNull(path) : builder.equal(path, object);
        if(result == null) {
            result = current;
            return this;
        }

        result = joinWithOr ? builder.or(result, current) : builder.and(result, current);
        return this;
    }

    <S> ConditionBuilder andEqual(Path<S> path, S object) {
        return joinEqual(path, object, false, false);
    }

    <S> ConditionBuilder orEqual(Path<S> path, S object) {
        return joinEqual(path, object, true, false);
    }

    <S> ConditionBuilder andEqualCheckingNull(Path<S> path, S object) {
        return joinEqual(path, object, false, true);
    }

    <S> ConditionBuilder orEqualCheckingNull(Path<S> path, S object) {
        return joinEqual(path, object, true, true);
    }

    <S> ConditionBuilder andNotEqual(Path<S> path, S object) {
        if(path != null && object != null) {
            result = result == null ? builder.notEqual(path, object) : builder.and(result, builder.notEqual(path, object));
        }
        return this;
    }

    <S, T> ConditionBuilder andIn(SetJoin<S, T> join, Collection<T> collection) {
        if(join != null && !isEmpty(collection)) {
            result = result == null ? join.in(collection) : builder.and(join.in(collection));
        }
        return this;
    }

    Predicate build() {
        return result;
    }
}
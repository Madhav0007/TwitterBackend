package com.integ.task.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryUtil {

    public static final String LIMIT_N_OFFSET = " LIMIT %s OFFSET %s";

    private QueryUtil() {
    }

    public static StringBuilder addPagination(Pageable pageable, StringBuilder stringBuilder) {
        return stringBuilder.append(
                String.format(LIMIT_N_OFFSET, pageable.getPageSize(), pageable.getOffset()));
    }

    public static StringBuilder addDateCondition(
            String column, String param, StringBuilder stringBuilder) {
        return stringBuilder.append("AND (DATE_FORMAT(" + column + ", '%d/%m/%Y') = :" + param + ") ");
    }

    public static StringBuilder addDateBetweenCondition(
            String column, String start, String end, StringBuilder stringBuilder) {
        return stringBuilder.append("AND ( " + column + " BETWEEN :" + start + " AND :" + end + " ) ");
    }

    public static String toILike(String str) {
        if (str == null) {
            return null;
        }
        return "%" + str + "%";
    }

    public static String toStartLike(String client) {
        return "%" + client;
    }

    public static String toEndLike(String str) {
        return str + "%";
    }

    public static StringBuilder addOrderBy(Pageable pageable, StringBuilder stringBuilder) {
        Sort sort = pageable.getSort();
        if (Objects.isNull(sort)) {
            return stringBuilder;
        }
        String orderBy =
                sort.stream()
                        .map(order -> String.format("%s %s", order.getProperty(), order.getDirection()))
                        .collect(Collectors.joining(", "));
        stringBuilder.append(" ORDER BY ").append(orderBy);
        return stringBuilder;
    }

    public static String toString(Object object) {
        return toString(object, "");
    }

    public static boolean toBoolean(Object object) {
        if (Objects.isNull(object)) {
            return false;
        }
        return (Boolean) object;
    }

    public static String toString(Object object, String ifNull) {
        if (Objects.isNull(object)) {
            return ifNull;
        }
        return object.toString();
    }

    public static Integer toInteger(Object object, Integer ifNull) {
        if (Objects.isNull(object)) {
            return ifNull;
        }
        return ((Number) object).intValue();
    }

    public static BigInteger toBigInteger(Object object) {
        if (Objects.isNull(object)) {
            return BigInteger.ZERO;
        }
        return ((BigInteger) object);
    }

    public static Integer toInteger(Object object) {
        return toInteger(object, null);
    }

    public static Double toDouble(Object object, Double ifNull) {
        if (Objects.isNull(object)) {
            return ifNull;
        }
        return ((Number) object).doubleValue();
    }

    public static Double toDouble(Object object) {
        return toDouble(object, null);
    }

    public static Long toLong(Object object) {
        return toLong(object, null);
    }

    public static Long toLong(Object object, Long ifNull) {
        if (Objects.isNull(object)) {
            return ifNull;
        }
        return ((Number) object).longValue();
    }

    public static String toString(List<String> values) {
        return values.stream().map(s -> String.format("'%s'", s)).collect(Collectors.joining(", "));
    }

    public static LocalDate toLocalDate(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }

        if (object instanceof java.sql.Date) {
            return ((java.sql.Date) object).toLocalDate();
        }

        if (object instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) object).toLocalDateTime().toLocalDate();
        }

        return null;
    }

    public static LocalDateTime toLocalDateTime(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }

        if (object instanceof java.sql.Date) {
            return ((java.sql.Date) object).toLocalDate().atStartOfDay();
        }

        if (object instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) object).toLocalDateTime();
        }

        return null;
    }

    public static MapSqlParameterSource buildMapParams(
            String key1, Object val1, String key2, Object val2) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(key1, val1);
        params.addValue(key2, val2);
        return params;
    }

    public static void main(String[] args) {
        System.out.println(toInteger(null, null));
    }
}

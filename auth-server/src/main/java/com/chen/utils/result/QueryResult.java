package com.chen.utils.result;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class QueryResult<T> {
    private List<T> list;
    private long total;
}

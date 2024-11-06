package com.chen.utils.result;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResponseResult extends ResponseResult {
    /**列表数据实体*/
    QueryResult queryResult;

    /**
     * 构造方法
     * @param resultCode
     * @param queryResult
     */
    public QueryResponseResult(ResultCode resultCode, QueryResult queryResult){
        super((CommonCode) resultCode);
        this.queryResult = queryResult;
    }

}

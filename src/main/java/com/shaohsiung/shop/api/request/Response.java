package com.shaohsiung.shop.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> extends BaseResponse {
    private T data;

    public Response(String message, Integer code, T data) {
        super(message, code);
        this.data = data;
    }
}

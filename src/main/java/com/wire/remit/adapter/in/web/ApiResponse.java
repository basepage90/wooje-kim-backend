package com.wire.remit.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(String status, int code, String messeage, T data) {

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>("success", 200, null, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("success", 200, null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", 500, message, null);
    }
}

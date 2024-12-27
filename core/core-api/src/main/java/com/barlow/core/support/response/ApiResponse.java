package com.barlow.core.support.response;

import com.barlow.core.support.error.CoreApiErrorMessage;
import com.barlow.core.support.error.CoreApiErrorType;

public class ApiResponse<T> {

	private final ResultType result;
	private final T data;
	private final CoreApiErrorMessage error;

	private ApiResponse(ResultType result, T data, CoreApiErrorMessage error) {
		this.result = result;
		this.data = data;
		this.error = error;
	}

	public static ApiResponse<?> success() {
		return new ApiResponse<>(ResultType.SUCCESS, null, null);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(ResultType.SUCCESS, data, null);
	}

	public static ApiResponse<?> error(CoreApiErrorType error) {
		return new ApiResponse<>(ResultType.ERROR, null, new CoreApiErrorMessage(error));
	}

	public static ApiResponse<?> error(CoreApiErrorType error, Object errorData) {
		return new ApiResponse<>(ResultType.ERROR, null, new CoreApiErrorMessage(error, errorData));
	}

	public ResultType getResult() {
		return result;
	}

	public Object getData() {
		return data;
	}

	public CoreApiErrorMessage getError() {
		return error;
	}
}

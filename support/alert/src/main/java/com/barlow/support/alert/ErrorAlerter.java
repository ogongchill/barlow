package com.barlow.support.alert;

import org.springframework.scheduling.annotation.Async;

@Async("asyncExecutor")
public interface ErrorAlerter {
	void alert(String message);
}

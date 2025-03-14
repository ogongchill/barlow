package com.barlow.support.alert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Component
public class SlackAlerter implements Alerter {

	private final SlackApi slackApi;

	public SlackAlerter(@Value("${alert.slack.webhook-url}") String webhookUrl) {
		slackApi = new SlackApi(webhookUrl);
	}

	@Override
	public void alert(String message) {
		slackApi.call(new SlackMessage(message));
	}
}

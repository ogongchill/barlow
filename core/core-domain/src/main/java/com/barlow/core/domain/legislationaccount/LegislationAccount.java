package com.barlow.core.domain.legislationaccount;

public class LegislationAccount {

	private final long no;
	private final String legislationType;
	private final String iconPath;
	private final String description;
	private final int postCount;
	private final int subscriberCount;
	private boolean isSubscribed;
	private boolean isNotifiable;

	public LegislationAccount(
		long no,
		String legislationType,
		String iconPath,
		String description,
		int postCount,
		int subscriberCount
	) {
		this.no = no;
		this.legislationType = legislationType;
		this.iconPath = iconPath;
		this.description = description;
		this.postCount = postCount;
		this.subscriberCount = subscriberCount;
	}

	void setSubscribed(boolean subscribed) {
		isSubscribed = subscribed;
	}

	void setNotifiable(boolean notifiable) {
		isNotifiable = notifiable;
	}

	public long getNo() {
		return no;
	}

	public String getLegislationType() {
		return legislationType;
	}

	public String getIconPath() {
		return iconPath;
	}

	public String getDescription() {
		return description;
	}

	public int getPostCount() {
		return postCount;
	}

	public int getSubscriberCount() {
		return subscriberCount;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public boolean isNotifiable() {
		return isNotifiable;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Long no;
		private String name;
		private String iconUrl;
		private String description;
		private Integer subscriberCount;
		private Integer postCount;

		public Builder no(Long no) {
			this.no = no;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder iconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder subscriberCount(Integer subscriberCount) {
			this.subscriberCount = subscriberCount;
			return this;
		}

		public Builder postCount(Integer postCount) {
			this.postCount = postCount;
			return this;
		}

		public LegislationAccount build() {
			return new LegislationAccount(
				this.no,
				this.name,
				this.iconUrl,
				this.description,
				this.postCount,
				this.subscriberCount
			);
		}
	}
}

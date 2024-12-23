package com.barlow.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device")
public class DeviceJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_no")
	private Long no;

	@Column(name = "device_id", nullable = false, length = 100)
	private String deviceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_os", nullable = false)
	private DeviceOs deviceOs;

	@Column(name = "token", nullable = false, length = 100)
	private String token;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_status", nullable = false)
	private Status status;

	enum Status {
		ACTIVE, INACTIVE;
	}
}

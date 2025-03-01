package com.barlow.storage.db.core;

import com.barlow.core.domain.account.Device;
import com.barlow.core.enumerate.DeviceOs;

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
	@Column(columnDefinition = "varchar(10)", name = "device_os", nullable = false)
	private DeviceOs deviceOs;

	@Column(name = "token", nullable = false, length = 100)
	private String token;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10)", name = "device_status", nullable = false)
	private Device.Status status;

	protected DeviceJpaEntity() {
	}

	public DeviceJpaEntity(String deviceId, DeviceOs deviceOs, String token, Long memberNo, Device.Status status) {
		this.deviceId = deviceId;
		this.deviceOs = deviceOs;
		this.token = token;
		this.memberNo = memberNo;
		this.status = status;
	}

	public DeviceJpaEntity(String deviceId, DeviceOs deviceOs, String token, Long memberNo) {
		this.deviceId = deviceId;
		this.deviceOs = deviceOs;
		this.token = token;
		this.memberNo = memberNo;
		this.status = Device.Status.ACTIVE;
	}

	Device toDevice() {
		return new Device(memberNo, deviceId, deviceOs, token, status);
	}

	void inactivate() {
		this.status = Device.Status.INACTIVE;
	}
}

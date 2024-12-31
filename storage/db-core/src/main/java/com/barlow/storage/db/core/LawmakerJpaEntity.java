package com.barlow.storage.db.core;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lawmaker")
public class LawmakerJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lawmaker_no")
	private Long no;

	@Column(name = "lawmaker_code", nullable = false)
	private String lawmakerCode;

	@Column(name = "korean_name", nullable = false)
	private String koreanName;

	@Enumerated(EnumType.STRING)
	@Column(name = "birth_calender_type", nullable = false)
	private BirthCalenderType birthCalenderType;

	@Column(name = "birth_date", nullable = false)
	private LocalDateTime birthDate;

	@Column(name = "job_responsibility_name")
	private String jobResponsibilityName; // 직책

	@Column(name = "party_name", nullable = false)
	private String partyName; // 정당명

	@Column(name = "orig_name", nullable = false)
	private String origName; // 선거구 이름

	@Column(name = "electoral_name", nullable = false)
	private String electoralName; // 지역구 or 비례대표

	@Column(name = "committees", length = 500)
	private String committees;

	@Column(name = "reelection_name", nullable = false)
	private String reelectionName; // 초선, 재선 등등

	@Column(name = "units", nullable = false)
	private String units; // 재선이면 여러 개의 대수 존재 : ex) 21대, 22대

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@Column(name = "telephone_no", nullable = false)
	private String telephoneNo;

	@Column(name = "email")
	private String email;

	@Column(name = "homepage")
	private String homepage;

	@Column(name = "staff", nullable = false)
	private String staff; // 보좌관

	@Column(name = "secretary", nullable = false)
	private String secretary; // 비서관

	@Column(name = "secretary2", nullable = false)
	private String secretary2; // 비서관 2

	@Column(columnDefinition = "text", name = "biography")
	private String biography;

	protected LawmakerJpaEntity() {
	}

	enum BirthCalenderType {
		LUNAR, SOLAR
	}

	enum Gender {
		MALE, FEMALE;
	}
}

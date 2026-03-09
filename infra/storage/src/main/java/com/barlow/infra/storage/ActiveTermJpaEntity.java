package com.barlow.infra.storage;

import com.barlow.core.domain.account.term.Term;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "active_term")
public class ActiveTermJpaEntity extends BaseTimeJpaEntity {

	@Id
	@Enumerated(value = EnumType.STRING)
	@Column(name = "term_type", nullable = false)
	private Term.Type termType;

	@Column(name = "term_no", nullable = false)
	private Long termNo;
}

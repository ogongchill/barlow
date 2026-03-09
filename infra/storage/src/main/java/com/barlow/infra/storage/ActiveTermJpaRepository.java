package com.barlow.infra.storage;

import com.barlow.core.domain.account.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveTermJpaRepository extends JpaRepository<ActiveTermJpaEntity, Term.Type> {}

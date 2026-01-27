package com.barlow.core.storage;

import com.barlow.core.domain.registration.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveTermJpaRepository extends JpaRepository<ActiveTermJpaEntity, Term.Type> {
}

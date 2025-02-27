package com.barlow.core.auth.authentication.id;

import com.barlow.core.auth.authentication.core.MemberPrincipal;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository {

    Optional<MemberPrincipal> findByMemberNo(Long memberNo);
}

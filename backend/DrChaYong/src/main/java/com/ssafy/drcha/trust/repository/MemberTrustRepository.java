package com.ssafy.drcha.trust.repository;

import com.ssafy.drcha.trust.entity.MemberTrust;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTrustRepository extends JpaRepository<MemberTrust, Long> {

    /**
     * 회원 ID로 MemberTrust를 조회합니다.
     * @param email 조회할 회원의 email
     * @return 해당 회원의 MemberTrust Optional
     */
    Optional<MemberTrust> findByEmail(String email);
}

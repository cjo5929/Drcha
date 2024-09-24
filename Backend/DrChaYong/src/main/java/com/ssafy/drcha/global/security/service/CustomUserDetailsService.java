package com.ssafy.drcha.global.security.service;

import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 주어진 사용자 이름으로 사용자 정보를 로드
     *
     * @param email 조회할 사용자의 이름
     * @return 로드된 사용자 정보를 담고 있는 UserDetails 객체
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
    }

    /**
     * Member 엔티티로부터 UserDetails 객체를 생성
     *
     * @param member UserDetails로 변환할 Member 엔티티
     * @return 생성된 UserDetails 객체
     */
    private UserDetails createUserDetails(Member member) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
        return User.builder()
                .username(member.getEmail())
                .password("")
                .authorities(Collections.singletonList(authority))
                .build();
    }
}
package com.ssafy.drcha.member.entity;

import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.member.enums.Role;
import com.ssafy.drcha.virtualaccount.entity.VirtualAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_key")
    private String userKey;

    @OneToMany(mappedBy = "creditor")
    private List<VirtualAccount> creditorAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "debtor")
    private List<VirtualAccount> debtorAccounts = new ArrayList<>();

    //==생성 메서드==//
    public static Member createMember(String username, String email, String avatarUrl, Role role, String userKey) {
        return Member.builder()
                .username(username)
                .email(email)
                .avatarUrl(avatarUrl)
                .role(role)
                .userKey(userKey)
                .build();
    }

    //==비즈니스 로직==//
    public void changeAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void changeUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void savePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void markAsVerified() {
        this.isVerified = true;
    }
}

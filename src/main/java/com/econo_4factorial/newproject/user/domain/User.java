package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.common.entity.BaseEntity;
import com.econo_4factorial.newproject.user.domain.vo.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email", nullable = false, unique = true)),
            @AttributeOverride(name = "name", column = @Column(name = "name", nullable=false)),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "phone_number", nullable = false))
    })
    private UserInfo userInfo;

    @Builder
    public User(String email, String name, String phoneNumber) {
        this.userInfo = new UserInfo(email, name, phoneNumber);
    }
}
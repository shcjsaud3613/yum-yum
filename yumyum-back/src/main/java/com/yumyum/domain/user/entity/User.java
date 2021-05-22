package com.yumyum.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yumyum.domain.user.dto.UpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_path")
    private String profilePath;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public void updateUser(final UpdateRequest dto){
        this.nickname = dto.getNickname();
        this.introduction = dto.getIntroduction();
        this.profilePath = dto.getProfilePath();
        this.modifiedDate = LocalDateTime.now();
    }
}
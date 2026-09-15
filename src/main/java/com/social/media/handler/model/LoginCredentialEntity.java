package com.social.media.handler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;

@Entity
@Table(name = "login_credentials")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialEntity {

    @Id
    @Column(name = "instagram_id", nullable = false)
    private String instagramId;

    @Column(name = "facebook_id", nullable = false)
    private String facebookId;

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column (name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column (name = "updated_at", nullable = false)
    private LocalDate updatedAt;
}

package com.social.media.handler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "login_credentials")
public class LoginCredentialEntity {

    @Id
    @Column(name = "instagram_id", nullable = false)
    private long instagramId;

    @Column(name = "facebook_id", nullable = false)
    private long facebookId;

    @Column (name = "access_token", nullable = false)
    private String accessToken;
}

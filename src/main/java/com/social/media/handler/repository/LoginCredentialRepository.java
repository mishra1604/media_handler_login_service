package com.social.media.handler.repository;

import com.social.media.handler.model.LoginCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginCredentialRepository extends JpaRepository<LoginCredentialEntity, Long> {

    @Query("SELECT l FROM LoginCredentialEntity l WHERE l.instagramId = :instagramId")
    LoginCredentialEntity findByInstagramId(long instagramId);
}

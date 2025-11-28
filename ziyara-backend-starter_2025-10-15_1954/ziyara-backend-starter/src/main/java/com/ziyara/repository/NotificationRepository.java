package com.ziyara.repository;

import com.ziyara.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUtilisateur_IdOrderByDateEnvoiDesc(Long userId, Pageable pageable);
    long deleteByUtilisateur_Id(Long userId); // delete all for user
}

package org.codej.instagram.repository;

import org.codej.instagram.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);

    Users findByProviderAndProviderId(String provider, String providerId);
}

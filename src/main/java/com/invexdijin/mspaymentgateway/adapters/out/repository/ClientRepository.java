package com.invexdijin.mspaymentgateway.adapters.out.repository;

import com.invexdijin.mspaymentgateway.application.core.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}

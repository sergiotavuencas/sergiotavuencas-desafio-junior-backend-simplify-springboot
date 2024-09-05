package br.com.tavuencas.sergio.desafio_simplify.domain.repository;

import br.com.tavuencas.sergio.desafio_simplify.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}

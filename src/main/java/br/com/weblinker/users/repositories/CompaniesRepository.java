package br.com.weblinker.users.repositories;

import br.com.weblinker.users.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompaniesRepository extends JpaRepository<Company, Long> {

    // TODO here maybe we do not need this
    Optional<Company> findFirstByOrderByIdAsc();
}

package com.centillion.userservice.repositories;


import com.centillion.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String value,
                                                                boolean deleted,
                                                                Date currentDate);
}

package ecommerce.userservice.repository;

import ecommerce.userservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {


    public Optional<Token> findByValueAndDeleted(String value, boolean deleted);

}

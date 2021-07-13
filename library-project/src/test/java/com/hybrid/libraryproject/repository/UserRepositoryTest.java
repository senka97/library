package com.hybrid.libraryproject.repository;

import com.hybrid.libraryproject.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void TestFindByEmailSuccess() {

        String email = "pera@gmail.com";

        User foundUser = repository.findByEmail(email);

        assertThat(foundUser.getEmail()).isEqualTo(email);
    }

    @Test
    public void TestFindByEmailUserNotFound() {

        String email = "user@email.com";

        User foundUser = repository.findByEmail(email);

        assertThat(foundUser).isNull();
    }

}

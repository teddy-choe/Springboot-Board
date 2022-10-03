package teddy.board.entity.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void createAndReadTest() {
        Role role = createRole();

        Role savedRole = roleRepository.save(role);
        clear();

        Role findRole = roleRepository.findById(savedRole.getId()).orElseThrow(NullPointerException::new);

        assertThat(findRole.getRoleType()).isEqualTo(savedRole.getRoleType());
    }

    private Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }
}
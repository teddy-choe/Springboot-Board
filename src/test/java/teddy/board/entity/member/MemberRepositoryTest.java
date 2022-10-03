package teddy.board.entity.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WebAppConfiguration
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void createAndReadTest() {
        Member member = createMember();

        memberRepository.save(member);
        clear();

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new);
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest() {
        Member member = createMember();

        memberRepository.save(member);
        clear();

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new);
        assertThat(foundMember.getCreatedAt()).isEqualTo(member.getCreatedAt());
    }

    @Test
    void updateTest() {
        String updateName = "updated";
        Member member = memberRepository.save(createMember());
        clear();

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new);
        foundMember.updateNickname(updateName);
        clear();

        Member updateMember = memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new);
        assertThat(updateMember.getNickname()).isEqualTo(updateName);
    }

    @Test
    void deleteTest() {
        Member member = memberRepository.save(createMember());
        clear();

        memberRepository.delete(member);
        clear();

        assertThatThrownBy(() -> memberRepository.findById(member.getId()).orElseThrow(NullPointerException::new)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void findByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(NullPointerException::new);

        // then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByNicknameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when
        Member foundMember = memberRepository.findByNickname(member.getNickname()).orElseThrow(NullPointerException::new);

        // then
        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void uniqueEmailTest() {
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember(member.getEmail(), "password2", "username2", "nickname2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void uniqueNicknameTest() {
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        //when, then
        assertThatThrownBy(() -> memberRepository.save(createMember("email2", "password2", "username2", member.getNickname())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void existsByEmailTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail() + "test")).isFalse();
    }

    @Test
    void existsByNicknameTest() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        assertThat(memberRepository.existsByNickname(member.getNickname())).isTrue();
        assertThat(memberRepository.existsByNickname(member.getNickname() + "test")).isFalse();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

//    private Member createMemberWithRoles(List<Role> roles) {
//        return new Member("email", "password", "username", "nickname", roles);
//    }

    private Member createMember(String email, String password, String username, String nickname) {
        return new Member(email, password, username, nickname, Collections.emptySet());
    }

    private Member createMember() {
        return new Member("email", "password", "username", "nickname", Collections.emptySet());
    }
}
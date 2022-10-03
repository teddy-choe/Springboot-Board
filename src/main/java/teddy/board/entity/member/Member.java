package teddy.board.entity.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teddy.board.entity.common.EntityDate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRole> roles;

    public Member(String email, String password, String username, String nickname, Set<MemberRole> roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}

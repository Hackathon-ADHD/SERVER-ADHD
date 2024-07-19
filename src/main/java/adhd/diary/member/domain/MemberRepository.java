package adhd.diary.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findBySocialId(String socialId);
}

package adhd.diary.diary.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<List<Diary>> findByMemberId(Long memberId);
}

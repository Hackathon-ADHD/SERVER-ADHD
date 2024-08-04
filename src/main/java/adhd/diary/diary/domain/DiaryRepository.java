package adhd.diary.diary.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<List<Diary>> findByMemberId(Long memberId);

    @Query("SELECT d FROM Diary d WHERE d.date = :targetDate")
    Optional<Diary> findLastYearByDate(@Param("targetDate") LocalDate targetDate);
}

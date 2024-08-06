package adhd.diary.diary.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {


    Optional<List<Diary>> findByMemberIdOrderByDateDesc(Long memberId);

    @Query(value = "SELECT * FROM diary d WHERE d.date = :targetDate", nativeQuery = true)
    Optional<Diary> findLastYearByDate(@Param("targetDate") LocalDate targetDate);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND d.date BETWEEN '2024-07-28' AND '2024-08-03'", nativeQuery = true)
    Optional<List<Diary>> findWeekeendByMemberId(Long memberId);
}

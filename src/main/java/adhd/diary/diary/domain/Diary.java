package adhd.diary.diary.domain;

import adhd.diary.common.BaseTimeEntity;
import adhd.diary.member.domain.Member;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    @Column(length = 1000)
    private String analyzedContents;

    @Column(length = 500)
    private String recommendSongs;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Diary(Long id, String content, String analyzedContents, String recommendSongs, Emotion emotion,
                 LocalDate date,
                 Member member) {
        this.id = id;
        this.content = content;
        this.analyzedContents = analyzedContents;
        this.recommendSongs = recommendSongs;
        this.emotion = emotion;
        this.date = date;
        this.member = member;
    }

    public Diary(String content, String analyzedContents, String recommendSongs, Emotion emotion, LocalDate date,
                 Member member) {
        this.content = content;
        this.analyzedContents = analyzedContents;
        this.recommendSongs = recommendSongs;
        this.emotion = emotion;
        this.date = date;
        this.member = member;
    }

    public Diary(String content, String analyzedContents, String recommendSongs, Emotion emotion, LocalDate date) {
        this.content = content;
        this.analyzedContents = analyzedContents;
        this.recommendSongs = recommendSongs;
        this.emotion = emotion;
        this.date = date;
    }

    public Diary(String content, Emotion emotion){
        this.content = content;
        this.emotion = emotion;
    }

    public String getAnalyzedContents() {
        return analyzedContents;
    }

    public String getRecommendSongs() {
        return recommendSongs;
    }

    public Diary() {}

    public LocalDate getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Member getMember() {
        return member;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}

package adhd.diary.diary.domain;

import adhd.diary.diary.common.BaseTimeEntity;
import adhd.diary.member.domain.Member;
import jakarta.persistence.*;

@Entity
public class Diary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Diary(String content, Emotion emotion){
        this.content = content;
        this.emotion = emotion;
    }

    public Diary() {}

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

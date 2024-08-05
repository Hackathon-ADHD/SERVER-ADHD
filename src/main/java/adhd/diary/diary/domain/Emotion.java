package adhd.diary.diary.domain;

public enum Emotion {

    JUST("그냥", 5),
    HAPPY("기분 좋아", 8),
    EXCITED("신나", 9),
    AMAZED("놀라워", 7),
    SAD("슬퍼", 2),
    LOVE("사랑이야", 10),
    CURIOUS("궁금해", 6),
    DISAPPOINTED("마음에 안들어", 3),
    HURT("아파", 1),
    IMPRESSED("역시 멋져", 9),
    ANGRY("화나", 2),
    DIZZY("어질어질해", 4);

    private final String type;
    private final int score;

    Emotion(String type, int score) {
        this.type = type;
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public int getScore() {
        return score;
    }
}

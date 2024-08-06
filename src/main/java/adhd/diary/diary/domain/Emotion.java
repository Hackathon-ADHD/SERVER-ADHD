package adhd.diary.diary.domain;

public enum Emotion {

    SOSO("그냥", 6),
    HAPPY("기분 좋아", 10),
    EXCITED("신나", 10),
    AMAZED("놀라워", 5),
    SAD("슬퍼", 2),
    LOVE("사랑이야", 10),
    CURIOUS("궁금해", 7),
    DISAPPOINTED("마음에 안들어", 2),
    HURT("아파", 2),
    IMPRESSED("역시 멋져", 9),
    ANGRY("화나", 1),
    DIZZY("어질어질해", 2);

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

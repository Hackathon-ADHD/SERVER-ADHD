package adhd.diary.diary.domain;

public enum Emotion {

    HAPPY("기분 좋아"),
    EXCITED("신나"),
    AMAZED("놀라워"),
    SAD("슬퍼"),
    LOVE("사랑이야"),
    CURIOUS("궁금해"),
    DISAPPOINTED("마음에 안들어"),
    HURT("아파"),
    IMPRESSED("역시 멋져"),
    ANGRY("화나"),
    DIZZY("어질어질해");

    private final String type;

    Emotion(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

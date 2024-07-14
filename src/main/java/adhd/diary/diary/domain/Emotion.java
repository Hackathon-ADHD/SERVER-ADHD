package adhd.diary.diary.domain;

public enum Emotion {

    HAPPINESS("행복"),
    SADNESS("슬픔"),
    ANGER("분노"),
    SURPRISE("놀람"),
    FEAR("두려움"),
    DISGUST("혐오"),
    EXCITEMENT("흥분"),
    LOVE("사랑"),
    JEALOUSY("질투"),
    GUILT("죄책감"),
    SHAME("수치심"),
    LONELINESS("외로움");

    private final String type;

    Emotion(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

package adhd.diary.response;

public class ApiBody <T> {

    private T data;

    public ApiBody(T data) {
        this.data = data;
    }
}

package personal.leo.kindlepush.model;

public class UserResult {

    private String accessToken;

    public UserResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

package model;

import io.vertx.core.json.JsonObject;

public class format {
    private String text;
    private String created_at;
    private boolean retweeted;
    private JsonObject user; // {"screen_name": "CottonKandy", "lang": "en","name": "CottonKandy","id": 281572434}
    private long id;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public JsonObject getUser() {
        return user;
    }

    public void setUser(JsonObject user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

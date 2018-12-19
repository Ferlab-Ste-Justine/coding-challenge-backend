package model;

import io.vertx.core.json.JsonObject;

public class format {
    private String text;
    private String created_at;
    private boolean retweeted;
    private JsonObject screen_name; // {"screen_name": "CottonKandy", "lang": "en","name": "CottonKandy","id": 281572434}
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

    public JsonObject getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(JsonObject screen_name) {
        this.screen_name = screen_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package model.entities;

import utils.JSONTools;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Album {

    private int userId;
    private int id;
    private String title;

    public Album(int userId, int id, String title) {
        this.userId = userId;
        this.id = id;
        this.title = title;
    }

    public Album(Map<String, String> record) {

        this(Integer.parseInt(record.get("userId")),
                Integer.parseInt(record.get("id")),
                record.get("title"));
    }

    public Album(String json) {

        this(JSONTools.getIntegerFromJSON(json, "userId"),
                JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getStringFromJSON(json, "title"));

    }

    public static Album getAlbum() {

        Random random = new Random();

        return new Album(random.nextInt(1001),
                random.nextInt(1001),
                "Sample Album");
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                {
                "userId": %d,
                "id": %d,
                "title": "%s"
                }
                """, userId, id, title);

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return userId == album.userId && id == album.id && Objects.equals(title, album.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, title);
    }
}

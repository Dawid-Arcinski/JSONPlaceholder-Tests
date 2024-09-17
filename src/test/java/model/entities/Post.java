package model.entities;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import net.datafaker.Faker;
import utils.JSONTools;

public class Post {

    private int userId;
    private int id;
    private String title;
    private String body;

    public Post(int id, int userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;

    }

    public Post(Map<String, String> record) {

        this(Integer.parseInt(record.get("id")),
                Integer.parseInt(record.get("userId")),
                record.get("title"),
                record.get("body"));
    }

    public Post(String json) {

        this(JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getIntegerFromJSON(json, "userId"),
                JSONTools.getStringFromJSON(json, "title"),
                JSONTools.getStringFromJSON(json, "body").replace("\n", "\\n"));

    }

    public static Post getPost() {

        Random random = new Random();
        Faker faker = new Faker();

        return new Post(random.nextInt(1001),
                random.nextInt(1001),
                faker.lorem().toString(),
                faker.lorem().toString());
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

    public String getBody() {
        return body;
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

    public void setBody(String body) {
        this.body = body;
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                {
                "id": %d,
                "userId": %d,
                "title": "%s",
                "body": "%s"
                }
                """, id, userId, title, body);

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return userId == post.userId && id == post.id && Objects.equals(title, post.title) && Objects.equals(body, post.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, title, body);
    }
}

package model.entities;

import net.datafaker.Faker;
import utils.JSONTools;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Comment {

    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;

    public Comment(int postId, int id, String name, String email, String body) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public Comment(Map<String, String> record) {

        this(Integer.parseInt(record.get("postId")),
                Integer.parseInt(record.get("id")),
                record.get("name"),
                record.get("email"),
                record.get("body"));
    }

    public Comment(String json) {

        this(JSONTools.getIntegerFromJSON(json, "postId"),
                JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getStringFromJSON(json, "name"),
                JSONTools.getStringFromJSON(json, "email"),
                JSONTools.getStringFromJSON(json, "body").replace("\n", "\\n"));

    }

    public static Comment getComment() {

        Random random = new Random();
        Faker faker = new Faker();

        return new Comment(random.nextInt(1001),
                random.nextInt(1001),
                "Sample Comment",
                faker.internet().emailAddress(),
                "Comment content.");
    }

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                {
                "postId": %d,
                "id": %d,
                "name": "%s",
                "email": "%s",
                "body": "%s"
                }
                """, postId, id, name, email, body);

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return postId == comment.postId && id == comment.id && Objects.equals(name, comment.name) && Objects.equals(email, comment.email) && Objects.equals(body, comment.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, id, name, email, body);
    }
}

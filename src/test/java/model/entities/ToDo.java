package model.entities;

import net.datafaker.Faker;
import utils.JSONTools;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ToDo {

    private int userId;
    private int id;
    private String title;
    private boolean completed;

    public ToDo(int userId, int id, String title, boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public ToDo(Map<String, String> record) {

        this(Integer.parseInt(record.get("userId")),
                Integer.parseInt(record.get("id")),
                record.get("title"),
                Boolean.parseBoolean(record.get("completed")));
    }

    public ToDo(String json) {

        this(JSONTools.getIntegerFromJSON(json, "userId"),
                JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getStringFromJSON(json, "title"),
                JSONTools.getBooleanFromJSON(json, "completed"));
    }

    public static ToDo getToDo() {

        Random random = new Random();
        Faker faker = new Faker();

        return new ToDo(random.nextInt(1001),
                random.nextInt(1001),
                faker.lorem().toString(),
                random.nextBoolean());
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                {
                "userId": %d,
                "id": %d,
                "title": "%s",
                "completed": %b
                }
                """, userId, id, title, completed);

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return userId == toDo.userId && id == toDo.id && completed == toDo.completed && Objects.equals(title, toDo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, title, completed);
    }
}



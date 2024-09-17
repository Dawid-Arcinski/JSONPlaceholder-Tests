package model.entities;

import net.datafaker.Faker;
import utils.JSONTools;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Photo {

    private int albumId;
    private int id;
    private String title;
    private String url;
    private String thumbnailUrl;

    public Photo(int albumId, int id, String title, String url, String thumbnailUrl) {
        this.albumId = albumId;
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Photo(Map<String, String> record) {

        this(Integer.parseInt(record.get("albumId")),
                Integer.parseInt(record.get("id")),
                record.get("title"),
                record.get("url"),
                record.get("thumbnailUrl"));
    }

    public Photo(String json) {

        this(JSONTools.getIntegerFromJSON(json, "albumId"),
                JSONTools.getIntegerFromJSON(json, "id"),
                JSONTools.getStringFromJSON(json, "title"),
                JSONTools.getStringFromJSON(json, "url"),
                JSONTools.getStringFromJSON(json, "thumbnailUrl"));

    }

    public static Photo getPhoto() {

        Random random = new Random();
        Faker faker = new Faker();

        return new Photo(random.nextInt(1001),
                random.nextInt(1001),
                "Sample Photo",
                faker.internet().url(false, false, true, true, false, false),
                faker.internet().url(false, false, true, true, false, false));
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String toJSONString() {

        String jsonBody = String.format("""
                {
                "albumId": %d,
                "id": %d,
                "title": "%s",
                "url": "%s",
                "thumbnailUrl": "%s"
                }
                """, albumId, id, title, url, thumbnailUrl);

        return jsonBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return albumId == photo.albumId && id == photo.id && Objects.equals(title, photo.title) && Objects.equals(url, photo.url) && Objects.equals(thumbnailUrl, photo.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, id, title, url, thumbnailUrl);
    }
}

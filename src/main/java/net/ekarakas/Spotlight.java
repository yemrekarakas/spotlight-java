package net.ekarakas;

import java.util.List;

public class Spotlight {
    private String title;
    private String date;
    private String landscapeId;
    private String portraitId;
    private String landscapeSource;
    private String portraitSource;
    private List<String> tags;

    public Spotlight() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLandscapeId() {
        return landscapeId;
    }

    public void setLandscapeId(String landscapeId) {
        this.landscapeId = landscapeId;
    }

    public String getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(String portraitId) {
        this.portraitId = portraitId;
    }

    public String getPortraitSource() {
        return portraitSource;
    }

    public void setPortraitSource(String portraitSource) {
        this.portraitSource = portraitSource;
    }

    public String getLandscapeSource() {
        return landscapeSource;
    }

    public void setLandscapeSource(String landscapeSource) {
        this.landscapeSource = landscapeSource;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Spotlight{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", landscapeId='" + landscapeId + '\'' +
                ", portraitId='" + portraitId + '\'' +
                ", landscapeSource='" + landscapeSource + '\'' +
                ", portraitSource='" + portraitSource + '\'' +
                ", tags=" + tags +
                '}';
    }
}

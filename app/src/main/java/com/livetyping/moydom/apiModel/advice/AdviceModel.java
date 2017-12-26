package com.livetyping.moydom.apiModel.advice;

/**
 * Created by Ivan on 25.12.2017.
 */

public class AdviceModel {
    public static final int STATUS_READED = 1;
    public static final int STATUS_DELETED = -1;

    private int adviceId;
    private int backgroundId;
    private String iconUrl;
    private String title;
    private String description;
    private int status;
    private String date;

    public AdviceModel() {
    }

    public int getAdviceId() {
        return adviceId;
    }

    public AdviceModel setAdviceId(int adviceId) {
        this.adviceId = adviceId;
        return this;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public AdviceModel setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
        return this;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public AdviceModel setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AdviceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AdviceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public AdviceModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getDate() {
        return date;
    }

    public AdviceModel setDate(String date) {
        this.date = date;
        return this;
    }
}

package Model;

public class TaskModel {
    private String status;
    private String title;
    private String date;
    private String time;
    private String description;

    public TaskModel(String status, String title, String date, String time, String description) {
        this.status = status;
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}

package org.maslov.ready_project;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Comparable<Task>{
    private long id;
    private String action;
    private LocalDateTime start;
    private LocalDateTime finish;


    public Task(long id, String action, LocalDateTime start, LocalDateTime finish) {
        this.id = id;
        this.action = action;
        this.start = start;
        this.finish = finish;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id = ");
        sb.append(this.getId());
        sb.append(System.lineSeparator());
        sb.append("action: '");
        sb.append(this.getAction());
        sb.append("'");
        sb.append(System.lineSeparator());
        sb.append("Started at ");
        String startDate = prepareStart(this.start);
        sb.append(startDate);
        sb.append(System.lineSeparator());
        Duration duration = Duration.between(LocalDateTime.now(), this.finish);
        String left = prepareDuration(duration);
        sb.append(left);
        return sb.toString();
    }

    private String prepareStart(LocalDateTime start) {
        StringBuilder sb = new StringBuilder();
        sb.append(start.getYear());
        sb.append("-");
        sb.append(start.getMonth().getValue());
        sb.append("-");
        sb.append(start.getDayOfMonth());
        sb.append(" (");
        sb.append(start.getDayOfWeek());
        sb.append("); ");
        sb.append(start.getHour());
        sb.append(":");
        sb.append(start.getHour());
        sb.append(":");
        sb.append(start.getSecond());
        return sb.toString();
    }

    private String prepareDuration(Duration duration) {
        String result = "";
        if (LocalDateTime.now().isBefore(this.finish)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Time will be finished at ");
            sb.append(duration.toDaysPart());
            sb.append(" day(s), ");
            sb.append(duration.toHoursPart());
            sb.append(" hour(s), ");
            sb.append(duration.toMinutesPart());
            sb.append(" minute(s) and ");
            sb.append(duration.toSecondsPart());
            sb.append(" seconds left.");
            result = sb.toString();
        } else {
            result = "Time has been finished!";
        }
        return result;
    }

    @Override
    public int compareTo(Task o) {
        int result = 0;
        if (this.getFinish().isBefore(o.getFinish())) {
            result = -1;
        }
        if (o.getFinish().isBefore(this.getFinish())) {
            result = 1;
        }
        return result;
    }
}

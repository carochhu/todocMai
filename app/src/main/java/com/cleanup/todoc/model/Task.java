package com.cleanup.todoc.model;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Comparator;

// Model for the tasks of the application
@Entity(foreignKeys = @ForeignKey(entity = Project.class,
            parentColumns = "id",
            childColumns = "projectId"))
public class Task {
    // The unique identifier of the task
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "projectId",index = true)
    private long projectId;
    @NonNull
    private String name;
    private long creationTimestamp;
    private Boolean isSelected;

    public long getProjectId() { return projectId; }
    public long getCreationTimestamp() { return creationTimestamp; }

    // Instantiates a new Task.
    // @param id                the unique identifier of the task to set
    // @param projectId         the unique identifier of the project associated to the task to set
    // @param name              the name of the task to set
    // @param creationTimestamp the timestamp when the task has been created to set
    public Task(long id, long projectId, @NonNull String name, long creationTimestamp) {
        this.setId(id);
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    @Ignore
    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.projectId = projectId;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }

    //GETTER
    public long getId() { return id; }
    @Nullable
    public Project getProject() { return Project.getProjectById(projectId); }
    @NonNull
    public String getName() { return name; }
    public Boolean getSelected() { return isSelected; }

    //SETTER
    private void setId(long id) { this.id = id; }
    private void setProjectId(long projectId) { this.projectId = projectId; }
    private void setName(@NonNull String name) { this.name = name; }
    private void setCreationTimestamp(long creationTimestamp) { this.creationTimestamp = creationTimestamp; }
    public void setSelected(Boolean selected) { isSelected = selected; }

    //  Comparator to sort task from A to Z
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    // Comparator to sort task from Z to A
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    // Comparator to sort task from last created to first created
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    // Comparator to sort task from first created to last created
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }
}

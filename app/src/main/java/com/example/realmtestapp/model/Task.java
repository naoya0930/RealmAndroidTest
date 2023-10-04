package com.example.realmtestapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;
public class Task extends RealmObject {
    @PrimaryKey private ObjectId id;
    private String name;
    // @Required private String status = TaskStatus.Open.name();
    // public void setStatus(TaskStatus status) { this.status = status.name(); }
    // public String getStatus() { return this.status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Task(String _name) { this.name = _name; }
    public Task() {}
}

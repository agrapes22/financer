package com.financer.persistence.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="types")
public class Type implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long typeId;

    private String typeName;
    private String typeCategory;

    public Type() {}

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getTypeCategory() {
        return typeCategory;
    }
    public void setTypeCategory(String typeCategory) {
        this.typeCategory = typeCategory;
    }

    
}

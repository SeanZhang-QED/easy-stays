package com.easy.easystays.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.Serializable;

@Document(indexName = "loc")
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @GeoPointField
    private GeoPoint geoPoint;

    public Location(int id, GeoPoint geoPoint) {
        this.id = id;
        this.geoPoint = geoPoint;
    }

    public Integer getId() {
        return id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}

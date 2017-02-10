package com.spp.banu.aluradmi.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by banu on 10/02/17.
 */

public class Vertex {
    final private String id;
    final private String name;
    final private LatLng location;


    public Vertex(String id, String name, LatLng location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    public String getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}

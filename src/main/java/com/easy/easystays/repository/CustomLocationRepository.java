package com.easy.easystays.repository;

import java.util.List;

public interface CustomLocationRepository {
    List<Integer> searchByDistance(double lat, double lon, String distance);
}

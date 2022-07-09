package com.easy.easystays.repository;

import com.easy.easystays.model.Location;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

//  https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations
// 	The most important part of the class name that corresponds to the fragment interface is the Impl postfix
public class CustomLocationRepositoryImpl implements CustomLocationRepository {
    private final String DEFAULT_DISTANCE = "50";
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public CustomLocationRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<Integer> searchByDistance(double lat, double lon, String distance) {
        if (distance == null || distance.isEmpty()) {
            distance = DEFAULT_DISTANCE;
        }

        // step 1: build the query
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withFilter(
                new GeoDistanceQueryBuilder("geoPoint")
                        .point(lat, lon)
                        .distance(distance, DistanceUnit.KILOMETERS)
        );

        // Step 2: do the search
        SearchHits<Location> searchResult = elasticsearchOperations.search(queryBuilder.build(), Location.class);
        List<Integer> locationIDs = new ArrayList<>();
        for (SearchHit<Location> hit : searchResult.getSearchHits()) {
            locationIDs.add(hit.getContent().getId());
        }
        return locationIDs;
    }
}

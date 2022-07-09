package com.easy.easystays.repository;

import com.easy.easystays.model.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends ElasticsearchRepository<Location, Integer>, CustomLocationRepository {

}
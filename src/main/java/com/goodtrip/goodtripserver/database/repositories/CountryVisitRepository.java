package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.CountryVisit;
import org.springframework.data.repository.CrudRepository;

public interface CountryVisitRepository extends CrudRepository<CountryVisit, Integer> {

    /**
     * Delete country visit by id.
     *
     * @param countryVisitId id of country visit.
     * @return 1 or more if country visit exists, 0 otherwise.
     */
    Integer deleteCountryVisitById(int countryVisitId);
}

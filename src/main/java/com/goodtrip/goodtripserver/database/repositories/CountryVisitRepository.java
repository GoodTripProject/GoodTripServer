package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.CountryVisit;
import org.springframework.data.repository.CrudRepository;

public interface CountryVisitRepository extends CrudRepository<CountryVisit, Integer> {

    /**
     * Delete country visit by id.
     *
     * @param countryVisitId id of country visit.
     * @return true if country visit exists, false otherwise.
     */
    Integer deleteCountryVisitById(int countryVisitId);
}

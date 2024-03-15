package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.models.*;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepositoryImplementation;
import com.goodtrip.goodtripserver.database.repositories.TripRepository;
import com.goodtrip.goodtripserver.database.repositories.TripRepositoryImplementation;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TripTests {
    TripRepository tripRepository = new TripRepositoryImplementation();

    static User user;

    private Trip createTrip(List<Note> notes, List<CountryVisit> visits) {
        tripRepository.addTrip(user.getId(), "1", 2, "aa", new Date(0), new Date(1), TripState.IN_PROCESS, notes, visits);
        List<Trip> trips = tripRepository.getTrips(user.getId());
        assertEquals(1, trips.size());
        return trips.getFirst();
    }

    private void deleteTrip(int tripId) {
        assertTrue(tripRepository.deleteTrip(tripId));
        assertTrue(tripRepository.getTrips(user.getId()).isEmpty());
    }

    @BeforeAll
    public static void setAuthenticationRepository() {
        AuthenticationRepository authenticationRepository = new AuthenticationRepositoryImplementation();
        String username = UUID.randomUUID().toString().substring(0, 31);
        String password = UUID.randomUUID().toString().substring(0, 31);
        authenticationRepository.signUpIfNotExists(username, UUID.randomUUID().toString().substring(0, 31), password, "a", "b", "");
        Optional<User> temp = authenticationRepository.login(username, password);
        if (temp.isEmpty()) {
            fail();
        }
        user = temp.get();
    }

    @AfterAll
    public static void deleteUser() {
        AuthenticationRepository authenticationRepository = new AuthenticationRepositoryImplementation();
        authenticationRepository.deleteUserIfExists(user.getUsername(), user.getHashedPassword());
    }

    @Test
    void addTrip_TripDoesNotExists_TripExistsAndDoesNotContainNotesAndVisits() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        assertEquals(user.getId(), trip.getUserId());
        assertEquals("1", trip.getTitle());
        assertEquals(2, trip.getMoneyInUsd());
        assertTrue(trip.getNotes().isEmpty());
        assertEquals(TripState.IN_PROCESS, trip.getState());
        deleteTrip(trip.getId());
    }

    @Test
    void deleteTrip_TripDoesNotExists_DeleteTripReturnsFalse() {
        assertFalse(tripRepository.deleteTrip(Integer.MAX_VALUE));
    }

    @Test
    void addNote_TripDoesNotExists_SeveralNotesIsAddedAndHaveSameTripId() {
        Trip trip = createTrip(List.of(new Note(UUID
                .randomUUID().toString(), null, UUID.randomUUID().toString())), new ArrayList<>());
        for (int index = 0; index < 10; ++index) {
            tripRepository.addNote(trip.getId(), new Note(UUID
                    .randomUUID().toString(), null, UUID.randomUUID().toString()));
            List<Note> notes = tripRepository.getTrips(user.getId()).getFirst().getNotes();
            assertEquals(notes.getFirst().getTripId(), trip.getId());

        }
        deleteTrip(trip.getId());
    }

    @Test
    void addDeleteNote_TripDoesNotExists_NotesIsAddedAndDeleted() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        tripRepository.addNote(trip.getId(), new Note(UUID
                .randomUUID().toString(), null, UUID.randomUUID().toString()));
        List<Note> notes = tripRepository.getTrips(user.getId()).getFirst().getNotes();
        assertEquals(1, notes.size());
        assertEquals(notes.getFirst().getTripId(), trip.getId());
        assertTrue(tripRepository.deleteNote(notes.getFirst().getId()));
        List<Trip> trips = tripRepository.getTrips(user.getId());
        assertEquals(1, trips.size());
        assertTrue(trips.getFirst().getNotes().isEmpty());
        deleteTrip(trip.getId());
    }

    @Test
    void addCountryVisit_TripDoesNotExists_CountryVisitWithoutCitiesIsAdded() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        assertTrue(tripRepository.addCountryVisit(trip.getId(), new CountryVisit("Country", Collections.emptyList())));
        List<CountryVisit> visits = getVisits();
        assertEquals(trip.getId(), visits.getFirst().getTripId());
        assertEquals("Country", visits.getFirst().getCountry());
        deleteTrip(trip.getId());
    }

    @NotNull
    private List<CountryVisit> getVisits() {
        List<Trip> trips = tripRepository.getTrips(user.getId());
        assertEquals(1, trips.size());
        List<CountryVisit> visits = trips.getFirst().getVisits();
        assertEquals(1, visits.size());
        return visits;
    }

    private void checkCityVisitsIsSame(CityVisit cityVisitExpected, CityVisit cityVisitActual) {
        assertEquals(cityVisitExpected.getCity(), cityVisitActual.getCity());
        assertEquals(cityVisitExpected.getCountryVisitId(), cityVisitActual.getCountryVisitId());
        assertEquals(cityVisitExpected.getLon(), cityVisitActual.getLon());
        assertEquals(cityVisitExpected.getLat(), cityVisitActual.getLat());
    }

    @Test
    void addCountryVisit_TripDoesNotExists_CountryVisitWithSeveralCitiesIsAdded() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        List<CityVisit> cityVisits = List.of(new CityVisit("City1", 1, 1),
                new CityVisit("City2", 2, 2), new CityVisit("City3", 3, 3));
        assertTrue(tripRepository.addCountryVisit(trip.getId(), new CountryVisit("Country", cityVisits)));
        List<CountryVisit> visits = getVisits();
        assertEquals(1, visits.size());
        List<CityVisit> addedCityVisits = visits.getFirst().getCities();
        assertEquals(cityVisits.size(), addedCityVisits.size());
        for (int index = 0; index < cityVisits.size(); ++index) {
            checkCityVisitsIsSame(cityVisits.get(index), addedCityVisits.get(index));
        }
        deleteTrip(trip.getId());
    }

    @Test
    void addDeleteCountryVisit_TripDoesNotExists_CountryVisitIsAddedAndDeleted(){
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        assertTrue(tripRepository.addCountryVisit(trip.getId(), new CountryVisit("Country", Collections.emptyList())));
        List<CountryVisit> actualCountryVisit = getCountryVisits(1);

        int countryVisitId = actualCountryVisit.getFirst().getId();
        tripRepository.deleteCountryVisit(countryVisitId);
        assertTrue(getCountryVisits(0).isEmpty());
        deleteTrip(trip.getId());
    }

    @NotNull
    private List<CountryVisit> getCountryVisits(int expectedCountOfCountryVisits) {
        List<Trip> trips = tripRepository.getTrips(user.getId());
        assertEquals(1,trips.size());
        List<CountryVisit> actualCountryVisit = tripRepository.getTrips(user.getId()).getFirst().getVisits();
        assertEquals(expectedCountOfCountryVisits,actualCountryVisit.size());
        return actualCountryVisit;
    }


}
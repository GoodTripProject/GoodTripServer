package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.configs.DatabaseConfig;
import com.goodtrip.goodtripserver.database.models.*;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import com.goodtrip.goodtripserver.database.repositories.CountryVisitRepository;
import com.goodtrip.goodtripserver.database.repositories.NoteRepository;
import com.goodtrip.goodtripserver.database.repositories.TripRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DatabaseConfig.class)
@ComponentScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EntityScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EnableJpaRepositories("com.goodtrip.goodtripserver.database.repositories")
@EnableAutoConfiguration(exclude = {JdbcTemplateAutoConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripTests {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    CountryVisitRepository countryVisitRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;
    User user;
    @Autowired
    private TransactionTemplate template;

    protected Trip createTrip(List<Note> notes, List<CountryVisit> visits) {
        tripRepository.saveTripAndWire(null,user.getId(), "1", 2, "aa", new Date(0), new Date(1), TripState.IN_PROCESS, notes, visits);
        List<Trip> trips = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId());
        return trips.getFirst();
    }

    @Transactional
    protected void deleteTrip(Integer tripId) {
        tripRepository.deleteTripById(tripId);
        assertTrue(tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId()).isEmpty());
    }

    @BeforeAll
    public void setAuthenticationRepository() {
        String username = UUID.randomUUID().toString().substring(0, 31);
        String password = UUID.randomUUID().toString().substring(0, 31);
        authenticationRepository.save(new User(username, UUID.randomUUID().toString().substring(0, 31), password, "a", "b", ""));
        Optional<User> temp = authenticationRepository.findUserByUsernameAndHashedPassword(username, password);
        if (temp.isEmpty()) {
            fail();
        }
        user = temp.get();
    }

    @AfterAll
    @Transactional
    public void deleteUser() {
        template.executeWithoutResult(status -> authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword(user.getUsername(), user.getHashedPassword()));
    }

    @Test
    @Transactional
    public void addTrip_TripDoesNotExists_TripExistsAndDoesNotContainNotesAndVisits() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        assertEquals(user.getId(), trip.getUserId());
        assertEquals("1", trip.getTitle());
        assertEquals(2, trip.getMoneyInUsd());
        assertTrue(trip.getNotes().isEmpty());
        assertEquals(TripState.IN_PROCESS, trip.getState());
        deleteTrip(trip.getId());
    }

    @Test
    @Transactional
    public void deleteTrip_TripDoesNotExists_DeleteTripReturnsFalse() {
        tripRepository.deleteTripById(Integer.MAX_VALUE);
    }


    @NotNull
    private List<CountryVisit> getVisits() {
        List<Trip> trips = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId());
        assertEquals(1, trips.size());
        List<CountryVisit> visits = trips.getFirst().getVisits();
        assertEquals(1, visits.size());
        return visits;
    }

    @NotNull
    private List<CountryVisit> getCountryVisits() {
        List<Trip> trips = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId());
        assertEquals(1, trips.size());
        List<CountryVisit> actualCountryVisit = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId()).getFirst().getVisits();
        assertEquals(1, actualCountryVisit.size());
        return actualCountryVisit;
    }


    @Test
    @Transactional
    public void getTripById_TripDoesNotExist_TripExistsAndCanBeGotById() {
        Trip trip = createTrip(Collections.emptyList(), Collections.emptyList());
        Optional<Trip> actualTrip = tripRepository.getTripById(trip.getId());
        if (actualTrip.isEmpty()) {
            fail();
        }
        assertEquals(trip.getId(), actualTrip.get().getId());
        assertEquals(trip.getUserId(), actualTrip.get().getUserId());
        assertEquals(trip.getTitle(), actualTrip.get().getTitle());
        assertEquals(trip.getState(), actualTrip.get().getState());
        assertEquals(trip.getMoneyInUsd(), actualTrip.get().getMoneyInUsd());
        assertEquals(trip.getDepartureDate(), actualTrip.get().getDepartureDate());
        assertEquals(trip.getArrivalDate(), actualTrip.get().getArrivalDate());
        deleteTrip(trip.getId());
    }

    @Test
    public void getTripById_TripDoesNotExist_TripDoesNotExistAndCannotBeGotById() {
        Optional<Trip> actualTrip = tripRepository.getTripById(Integer.MAX_VALUE);
        assertTrue(actualTrip.isEmpty());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class NotesTests {
        Trip trip;

        private Note saveNote() {
            return noteRepository.save(new Note("a", null,"nu", "b", trip.getId()));
        }

        @Transactional
        protected void dropNote(Integer id) {
            noteRepository.deleteNoteById(id);
        }

        @BeforeAll
        void setupTrip() {
            trip = createTrip(Collections.emptyList(), Collections.emptyList());
        }

        @AfterAll
        void dropTrip() {
            template.executeWithoutResult(status -> tripRepository.deleteTripById(trip.getId()));
        }

        @Test
        public void getNoteById_NoteDoesNotExist_NoteDoesNotExistAndCannotBeGettedById() {
            Optional<Note> actualNote = noteRepository.getNoteById(Integer.MAX_VALUE);
            assertTrue(actualNote.isEmpty());
        }

        @Test
        @Transactional
        public void getNoteById_NoteExists_NoteCanBeGettedById() {
            Note note = saveNote();
            assertEquals("a", note.getTitle());
            assertEquals("b", note.getGooglePlaceId());
            dropNote(note.getId());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CountryVisitTests {
        Trip trip;

        @BeforeAll
        void setupTrip() {
            trip = createTrip(Collections.emptyList(), Collections.emptyList());
        }

        @AfterAll
        void dropTrip() {
            template.executeWithoutResult(status -> tripRepository.deleteTripById(trip.getId()));

        }


        @Test
        @Transactional
        public void addDeleteCountryVisit_TripDoesNotExists_CountryVisitIsAddedAndDeleted() {
            countryVisitRepository.save(new CountryVisit("Country", Collections.emptyList(), trip.getId()));
            List<CountryVisit> actualCountryVisit = getCountryVisits();
            int countryVisitId = actualCountryVisit.getFirst().getId();
            assertTrue(countryVisitRepository.deleteCountryVisitById(countryVisitId) > 0);
        }

        @Test
        @Transactional
        public void addCountryVisit_TripDoesNotExists_CountryVisitWithoutCitiesIsAdded() {
            countryVisitRepository.save(new CountryVisit("Country", Collections.emptyList(), trip.getId()));
            List<CountryVisit> visits = getVisits();
            assertEquals(trip.getId(), visits.getFirst().getTripId());
            assertEquals("Country", visits.getFirst().getCountry());
        }
    }
}
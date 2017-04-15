package de.fisp.skp.jpa;

import de.fisp.skp.model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, String> {
    List<Reservation> findByDarlehen(String darlehen);
}
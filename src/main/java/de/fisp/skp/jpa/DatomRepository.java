package de.fisp.skp.jpa;

import de.fisp.skp.model.Datom;
import de.fisp.skp.model.DatomId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DatomRepository extends CrudRepository<Datom, DatomId> {
    List<Datom> findByDatomId(DatomId datomId);

    @Query("select d from Datom d where d.datomId.darlehen = :darlehen")
    List<Datom> findByDarlehen(@Param("darlehen") String darlehen);

}
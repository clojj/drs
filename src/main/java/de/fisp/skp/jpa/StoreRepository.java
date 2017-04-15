package de.fisp.skp.jpa;

import de.fisp.skp.model.Store;
import de.fisp.skp.model.StoreId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends CrudRepository<Store, StoreId> {
    List<Store> findByStoreId(StoreId storeId);

    @Query("select store from Store store where store.storeId.darlehen = :darlehen")
    List<Store> findByDarlehen(@Param("darlehen") String darlehen);

}
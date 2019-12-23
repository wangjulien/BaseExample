package com.centreon.map.repository;
import com.centreon.map.domain.Host;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Host entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostRepository extends JpaRepository<Host, Long> {

}

package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, String> {
}

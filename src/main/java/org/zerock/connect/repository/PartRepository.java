package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {
}

package org.zerock.connect.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Assy;

@Repository
public interface AssyRepository extends JpaRepository<Assy, String> {
}

package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.entity.Releases;

import java.util.List;

@Repository
public interface ReleasesRepository extends JpaRepository<Releases, Long> {






}

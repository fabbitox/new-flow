package fabbitox.flow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fabbitox.flow.entity.Target;

public interface TargetRepository extends JpaRepository<Target, Integer> {

}

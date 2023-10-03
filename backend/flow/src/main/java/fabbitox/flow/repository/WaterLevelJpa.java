package fabbitox.flow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fabbitox.flow.entity.WaterLevel;
import fabbitox.flow.entity.WaterLevelId;

public interface WaterLevelJpa extends JpaRepository<WaterLevel, WaterLevelId>, WaterLevelRepository {
	@Override
	default void insertWaterLevel(WaterLevel waterLevel) {
		save(waterLevel);
	}
}

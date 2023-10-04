package fabbitox.flow.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import fabbitox.flow.entity.WaterLevel;
import fabbitox.flow.entity.WaterLevelId;
import lombok.RequiredArgsConstructor;

//@Repository
//@Primary
@RequiredArgsConstructor
public class WaterLevelJdbc implements WaterLevelRepository {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void insertWaterLevel(WaterLevel waterLevel) {
		WaterLevelId id = waterLevel.getId();
		jdbcTemplate.update("INSERT INTO water_level (target_id, observe_datetime, water_level) VALUES (?, ?, ?)",
				id.getTargetId(), id.getObserveDatetime(), waterLevel.getWaterLevel());
	}
}

package fabbitox.flow.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fabbitox.flow.entity.WaterLevel;
import fabbitox.flow.entity.WaterLevelId;
import lombok.RequiredArgsConstructor;

@Repository
@Primary
@RequiredArgsConstructor
public class WaterLevelJdbc implements WaterLevelRepository {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void insertWaterLevel(WaterLevel waterLevel) {
		WaterLevelId id = waterLevel.getId();
		final Integer targetId = id.getTargetId();
		final String observeDatetime = id.getObserveDatetime();
		
		int count = jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM water_level where target_id = %d and observe_datetime = \"%s\"", targetId, observeDatetime), Integer.class);
		if (count > 0)
			return;
		
		jdbcTemplate.update("INSERT INTO water_level (target_id, observe_datetime, water_level) VALUES (?, ?, ?)",
				targetId, observeDatetime, waterLevel.getWaterLevel());
	}
}

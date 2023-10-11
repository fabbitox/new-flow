package fabbitox.flow.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fabbitox.flow.entity.AwsValue;
import fabbitox.flow.entity.Input;
import fabbitox.flow.entity.Predict;
import fabbitox.flow.entity.PredictResult;
import lombok.RequiredArgsConstructor;

@Repository
@Primary
@RequiredArgsConstructor
public class PredictJdbc implements PredictRepository {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void insertPredict(Predict predict) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO predict (predict_time, request_time, target_id) VALUES (?, ?, ?)", new String[] { "id" });
			ps.setTimestamp(1, Timestamp.valueOf(predict.getPredictTime()));
			ps.setTimestamp(2, Timestamp.valueOf(predict.getRequestTime()));
			ps.setInt(3, predict.getTarget().getId());
			return ps;
		}, keyHolder);
		Integer predictId = keyHolder.getKey().intValue();

		List<Input> inputs = predict.getInputs();
		for (int i = 0; i < inputs.size(); i++) {
			final int sequence = i + 1;
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO input (sequence, predict_id) VALUES (?, ?)", new String[] { "id" });
				ps.setInt(1, sequence);
				ps.setInt(2, predictId);
				return ps;
			}, keyHolder);
			Integer inputId = keyHolder.getKey().intValue();

			List<AwsValue> awsValues = inputs.get(i).getAwsValues();
			for (AwsValue awsValue : awsValues) {
				jdbcTemplate.update(
						"INSERT INTO aws_value (temperature, wind_direction, wind_speed, rainfall, humidity, aws_id, input_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
						awsValue.getTemperature(), awsValue.getWindDirection(), awsValue.getWindSpeed(),
						awsValue.getRainfall(), awsValue.getHumidity(), awsValue.getAws().getId(), inputId);
			}
		}
		
		List<PredictResult> results = predict.getResults();
		for (int i = 0; i < results.size(); i++) {
			PredictResult result = results.get(i);
			jdbcTemplate.update(
					"INSERT INTO predict_result (hour, water_level, predict_id) VALUES (?, ?, ?)",
					result.getHour(), result.getWaterLevel(), predictId);
		}
	}
}

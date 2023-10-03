package fabbitox.flow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PredictInput {
	private Double[][][] fcstValues;
	private Double[] waterLevels;
	
	public PredictInput(Double[][][] fcstValues, Double[] waterLevels) {
		this.fcstValues = fcstValues;
		this.waterLevels = waterLevels;
	}
}

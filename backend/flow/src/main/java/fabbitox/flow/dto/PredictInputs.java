package fabbitox.flow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PredictInputs {
	private String[][][] fcstValues;
	private Double[] waterLevels;
	
	public PredictInputs(String[][][] fcstValues, Double[] waterLevels) {
		this.fcstValues = fcstValues;
		this.waterLevels = waterLevels;
	}
}

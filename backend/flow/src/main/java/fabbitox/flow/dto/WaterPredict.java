package fabbitox.flow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaterPredict {
	private Double[][][] inputs;
	private Double[] waters;
	private Double[] results;
	private String start;
	private String requestTime;
	
	public WaterPredict(Double[][][] inputs, Double[] waters, Double[] results, String start, String requestTime) {
		this.inputs = inputs;
		this.waters = waters;
		this.results = results;
		this.start = start;
		this.requestTime = requestTime;
	}
}

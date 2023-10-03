package fabbitox.flow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaterPredict {
	private Double[] waters;
	private Double[] results;
	private String start;
	private String requestTime;
	
	public WaterPredict(Double[] waters, Double[] results, String start, String requestTime) {
		this.waters = waters;
		this.results = results;
		this.start = start;
		this.requestTime = requestTime;
	}
}

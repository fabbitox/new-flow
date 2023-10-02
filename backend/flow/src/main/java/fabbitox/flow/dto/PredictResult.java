package fabbitox.flow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PredictResult {
	private Double[] waters;
	private Double[] results;
	private String start;
	
	public PredictResult(Double[] waters, Double[] results, String start) {
		super();
		this.waters = waters;
		this.results = results;
		this.start = start;
	}
}

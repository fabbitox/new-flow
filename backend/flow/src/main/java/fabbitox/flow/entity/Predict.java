package fabbitox.flow.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Predict {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDateTime predictTime;
	private LocalDateTime requestTime;
	@OneToMany(mappedBy = "predict", cascade = CascadeType.ALL)
	private List<Input> inputs;
	@OneToMany(mappedBy = "predict", cascade = CascadeType.ALL)
	private List<PredictResult> results;
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "target_id", unique = true)
	private Target target;

	public Predict(LocalDateTime predictTime, LocalDateTime requestTime, List<Input> inputs,
			List<PredictResult> results, Target target) {
		this.predictTime = predictTime;
		this.requestTime = requestTime;
		this.inputs = inputs;
		for (Input input: inputs) {
			input.setPredict(this);
		}
		this.results = results;
		for (PredictResult result: results) {
			result.setPredict(this);
		}
		this.target = target;
		target.setPredict(this);
	}

	public LocalDateTime getPredictTime() {
		return predictTime;
	}
	public LocalDateTime getRequestTime() {
		return requestTime;
	}
	public List<Input> getInputs() {
		return inputs;
	}
	public List<PredictResult> getResults() {
		return results;
	}
	public Target getTarget() {
		return target;
	}
}

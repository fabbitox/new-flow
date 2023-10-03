package fabbitox.flow.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Predict {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDateTime predictTime;
	private LocalDateTime requestTime;
	@OneToMany(mappedBy = "predict", cascade = CascadeType.ALL)
	private List<PredictResult> results;
	@OneToMany(mappedBy = "predict", cascade = CascadeType.ALL)
	private List<Input> inputs;
}

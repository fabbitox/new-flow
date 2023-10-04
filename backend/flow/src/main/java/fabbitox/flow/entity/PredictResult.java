package fabbitox.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PredictResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer hour;
	private Double waterLevel;
	@ManyToOne
	@JoinColumn(name = "predict_id")
	private Predict predict;
	
	public PredictResult(Integer hour, Double waterLevel) {
		this.hour = hour;
		this.waterLevel = waterLevel;
	}
	
	public Integer getHour() {
		return hour;
	}
	public Double getWaterLevel() {
		return waterLevel;
	}
	public void setPredict(Predict predict) {
		this.predict = predict;
	}
}

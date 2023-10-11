package fabbitox.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Target {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@OneToOne(mappedBy = "target")
	private Predict predict;
	
	public Integer getId() {
		return id;
	}
	public void setPredict(Predict predict) {
		this.predict = predict;
	}
}

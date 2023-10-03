package fabbitox.flow.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Input {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer sequence;
	@ManyToOne
	@JoinColumn(name = "predict_id")
	private Predict predict;
	@OneToMany(mappedBy = "input", cascade = CascadeType.ALL)
	private List<AwsValue> awsValues;
	
	public Input(Integer sequence, List<AwsValue> awsValues) {
		this.sequence = sequence;
		this.awsValues = awsValues;
		for (AwsValue awsValue: awsValues) {
			awsValue.setInput(this);
		}
	}
	
	public void setPredict(Predict predict) {
		this.predict = predict;
	}
}

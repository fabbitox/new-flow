package fabbitox.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AwsValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double temperature;
	private Double windDirection;
	private Double windSpeed;
	private Double rainfall;
	private Double humidity;
	@ManyToOne
	@JoinColumn(name = "aws_id")
	private Aws aws;
	@ManyToOne
	@JoinColumn(name = "input_id")
	private Input input;
	
	public AwsValue(Double temperature, Double windDirection, Double windSpeed, Double rainfall, Double humidity,
			Aws aws) {
		this.temperature = temperature;
		this.windDirection = windDirection;
		this.windSpeed = windSpeed;
		this.rainfall = rainfall;
		this.humidity = humidity;
		this.aws = aws;
	}
	
	public void setInput(Input input) {
		this.input = input;
	}
}

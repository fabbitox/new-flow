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
}

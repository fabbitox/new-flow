package fabbitox.flow.entity;

import javax.validation.Valid;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WaterLevel {
	@EmbeddedId
	@Valid
	private WaterLevelId id;
	private Double waterLevel;
	@ManyToOne
	@MapsId("targetId")
	@JoinColumn(name = "target_id")
	private Target target;
	
	public WaterLevelId getId() {
		return id;
	}
	public Double getWaterLevel() {
		return waterLevel;
	}
}

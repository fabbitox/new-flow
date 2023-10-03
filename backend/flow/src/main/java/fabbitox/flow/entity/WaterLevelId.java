package fabbitox.flow.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class WaterLevelId implements Serializable {
	private Integer targetId;
	private String observeDatetime;
}

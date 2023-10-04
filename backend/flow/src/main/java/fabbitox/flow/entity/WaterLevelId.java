package fabbitox.flow.entity;

import java.io.Serializable;

import javax.validation.constraints.Size;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WaterLevelId implements Serializable {
	private Integer targetId;
	@Size(min = 10, max = 10, message = "observeDatetime form: yyyyMMddHH")
	private String observeDatetime;
}

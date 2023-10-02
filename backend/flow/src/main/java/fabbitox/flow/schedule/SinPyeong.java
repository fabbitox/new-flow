package fabbitox.flow.schedule;

import org.springframework.stereotype.Component;

@Component
public class SinPyeong extends PredictSchedule {
	public SinPyeong() {
		super(3, new String[] {"96", "76", "97", "76", "96", "73"}, "2302695", 1);
	}
}

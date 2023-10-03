package fabbitox.flow.schedule;

import org.springframework.stereotype.Component;

import fabbitox.flow.entity.Aws;
import fabbitox.flow.service.AwsService;
import fabbitox.flow.service.PredictService;
import fabbitox.flow.service.TargetService;
import fabbitox.flow.service.WaterLevelService;

@Component
public class SinPyeong extends PredictSchedule {
	public SinPyeong(AwsService awsService, TargetService targetService, PredictService predictService, WaterLevelService waterLevelService) {
		super(3, new Aws[] { awsService.findById(1), awsService.findById(2), awsService.findById(3) },
				new String[] { "96", "76", "97", "76", "96", "73" }, "2302695", 1, targetService.findById(1), predictService, waterLevelService);
	}
}

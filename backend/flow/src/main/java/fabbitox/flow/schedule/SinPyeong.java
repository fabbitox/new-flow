package fabbitox.flow.schedule;

import org.springframework.stereotype.Component;

import fabbitox.flow.entity.Aws;
import fabbitox.flow.service.AwsService;
import fabbitox.flow.service.PredictServiceImpl;
import fabbitox.flow.service.TargetService;
import fabbitox.flow.service.WaterLevelServiceImpl;

@Component
public class SinPyeong extends PredictSchedule {
	public SinPyeong(AwsService awsService, TargetService targetService, PredictServiceImpl predictService, WaterLevelServiceImpl waterLevelService) {
		super(3, new Aws[] { awsService.findById(1), awsService.findById(2), awsService.findById(3) },
				new String[] { "96", "76", "97", "76", "96", "73" }, "2302695", 1, targetService.findById(1), predictService, waterLevelService);
	}
}

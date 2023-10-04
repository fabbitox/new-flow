package fabbitox.flow.service;

import org.springframework.stereotype.Service;

import fabbitox.flow.entity.WaterLevel;
import fabbitox.flow.repository.WaterLevelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaterLevelServiceImpl implements WaterLevelService {
	private final WaterLevelRepository waterLevelRepository;
	
	@Transactional
	@Override
	public void insertWaterLevel(WaterLevel waterLevel) {
		waterLevelRepository.insertWaterLevel(waterLevel);
	}
}

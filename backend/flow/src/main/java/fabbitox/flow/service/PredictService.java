package fabbitox.flow.service;

import org.springframework.stereotype.Service;

import fabbitox.flow.entity.Predict;
import fabbitox.flow.repository.PredictRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PredictService {
	private final PredictRepository predictRepository;
	
	@Transactional
	public void insertPredict(Predict predict) {
		predictRepository.insertPredict(predict);
	}
}

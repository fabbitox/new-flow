package fabbitox.flow.service;

import org.springframework.stereotype.Service;

import fabbitox.flow.entity.Target;
import fabbitox.flow.repository.TargetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TargetService {
	private final TargetRepository targetRepository;
	
	public Target findById(Integer id) {
		return targetRepository.findById(id).get();
	}
}

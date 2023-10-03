package fabbitox.flow.service;

import org.springframework.stereotype.Service;

import fabbitox.flow.entity.Aws;
import fabbitox.flow.repository.AwsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsService {
	private final AwsRepository awsRepository;
	
	public Aws findById(Integer id) {
		return awsRepository.findById(id).get();
	}
}

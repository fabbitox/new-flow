package fabbitox.flow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fabbitox.flow.entity.Predict;

public interface PredictJpa extends JpaRepository<Predict, Integer>, PredictRepository {
	@Override
	default void insertPredict(Predict predict) {
		save(predict);
	}
}

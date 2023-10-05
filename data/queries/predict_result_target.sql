select predict_result.water_level
from predict, predict_result
where predict.id = predict_result.predict_id and predict.id in (
	select distinct input.predict_id
	from target, aws, aws_value, input
	where target.id = aws.target_id and aws.id = aws_value.aws_id and input.id = aws_value.input_id
)
order by predict.id
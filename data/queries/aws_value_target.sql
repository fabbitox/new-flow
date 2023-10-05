select aws_value.temperature, aws_value.wind_direction, aws_value.wind_speed, aws_value.rainfall, aws_value.humidity
from target, aws, aws_value
where target.id = aws.target_id and aws.id = aws_value.aws_id and target.id = 1
order by aws_value.aws_id
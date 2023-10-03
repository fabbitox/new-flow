package fabbitox.flow.schedule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fabbitox.flow.dto.PredictInput;
import fabbitox.flow.dto.WaterPredict;
import fabbitox.flow.entity.Aws;
import fabbitox.flow.entity.AwsValue;
import fabbitox.flow.entity.Input;
import fabbitox.flow.entity.Predict;
import fabbitox.flow.entity.PredictResult;
import fabbitox.flow.entity.Target;
import fabbitox.flow.entity.WaterLevel;
import fabbitox.flow.entity.WaterLevelId;
import fabbitox.flow.service.PredictService;
import fabbitox.flow.service.WaterLevelService;
import fabbitox.flow.websocket.WebSocketHandler;
import reactor.core.publisher.Mono;

public class PredictSchedule {
	@Value("${fcst.url}")
	private String fcstUrl;
	@Value("${fcst.service-key}")
	private String serviceKey;
	@Value("${water.url}")
	private String waterUrl;
	@Value("${flask.url}")
	private String flaskUrl;

	private int awsCount;
	private Aws[] awss;
	private String[] xys;
	private String obscd;
	private int id;
	private Target target;

	private LocalDateTime requestTime;
	private LocalDateTime base;
	private DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");
	private Double[][][] fcstValues;
	private String[] waterHours = new String[7];
	private Double[] waterLevels = new Double[7];
	private String waterStart;
	private Double[] results = new Double[3];

	private final PredictService predictService;
	private final WaterLevelService waterLevelService;

	public PredictSchedule(int awsCount, Aws[] awss, String[] xys, String obscd, int id, Target target,
			PredictService predictService, WaterLevelService waterLevelService) {
		this.awsCount = awsCount;
		this.awss = awss;
		this.xys = xys;
		this.obscd = obscd;
		this.id = id;
		this.target = target;
		fcstValues = new Double[awsCount][3][5];
		this.predictService = predictService;
		this.waterLevelService = waterLevelService;
	}

	@Scheduled(fixedRate = 200000)
	public void task() {
		requestTime = LocalDateTime.now();
		int minute = requestTime.getMinute();
		if (minute < 45)
			base = requestTime.minusMinutes(minute + 30);
		else
			base = requestTime.minusMinutes(minute - 30);
		DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HHmm");
		String baseDate = base.format(ymd);
		String baseTime = base.format(hhmm);
		try {
			getFcst(baseDate, baseTime);
			getWater(minute);
			postPredict();
		} catch (Exception e) {
			System.out.println("오류 발생: " + e.getMessage());
		}
	}

	private void getFcst(String baseDate, String baseTime)
			throws IOException, InterruptedException, JsonProcessingException, JsonMappingException {
		for (int i = 0; i < awsCount; i++) {
			String url = String.format(fcstUrl, serviceKey, baseDate, baseTime, xys[i * 2], xys[i * 2 + 1]);
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int[] baseIndexes = new int[] { 4, 8, 9, 2, 5 };
			if (response.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode data = objectMapper.readTree(response.body());
				JsonNode item = data.get("response").get("body").get("items").get("item");
				for (int j = 0; j < 5; j++) {
					for (int k = 0; k < 3; k++) {
						if (j == 3) {
							String rainfall = item.get(baseIndexes[j] * 6 + k).get("fcstValue").asText();
							if (rainfall.equals("강수없음")) {
								fcstValues[i][k][j] = 0.0;
							} else {
								fcstValues[i][k][j] = Double.parseDouble(rainfall.substring(0, rainfall.length() - 2));
							}
						} else {
							fcstValues[i][k][j] = item.get(baseIndexes[j] * 6 + k).get("fcstValue").asDouble();
						}
					}
				}
			} else {
				System.out.println("오류 응답: " + response.statusCode());
				return;
			}
		}
	}

	private void getWater(int minute)
			throws IOException, InterruptedException, JsonProcessingException, JsonMappingException {
		String url = String.format(waterUrl, obscd, requestTime.minusMinutes(10).minusHours(7).format(ymd));
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() == 200) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode data = objectMapper.readTree(response.body()).get("list");
			int startIndex = data.size() - 7;
			if (minute >= 10 && minute < 45)
				startIndex -= 1;
			for (int j = 0; j < 7; j++) {
				waterHours[j] = data.get(startIndex + j).get("ymdh").asText();
				waterLevels[j] = data.get(startIndex + j).get("wl").asDouble();
			}
			waterStart = waterHours[0];
		} else {
			System.out.println("오류 응답: " + response.statusCode());
			return;
		}
	}

	private void postPredict() {
		String url = flaskUrl + "/predict/" + id;
		WebClient webClient = WebClient.builder().baseUrl(url).build();
		Mono<String> responseMono = webClient.post().uri("").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(new PredictInput(fcstValues, waterLevels))).retrieve()
				.bodyToMono(String.class);
		responseMono.subscribe(responseBody -> {
			ObjectMapper om = new ObjectMapper();
			try {
				JsonNode jsonResult = om.readTree(responseBody);
				for (int i = 0; i < 3; i++) {
					results[i] = jsonResult.get(0).get(i).asDouble();
				}
				WebSocketHandler.sendData(om.writeValueAsString(new WaterPredict(waterLevels, results, waterStart,
						requestTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")))));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			save();
		}, error -> System.err.println("Error: " + error.getMessage()));
	}

	private void save() {
		List<Input> inputs = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			List<AwsValue> awsValues = new ArrayList<>();
			for (int j = 0; j < awsCount; j++) {
				awsValues.add(new AwsValue(fcstValues[j][i][0], fcstValues[j][i][1], fcstValues[j][i][2],
						fcstValues[j][i][3], fcstValues[j][i][4], awss[j]));
			}
			inputs.add(new Input(i + 1, awsValues));
		}
		List<PredictResult> predictResults = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			predictResults.add(new PredictResult(i + 1, results[i]));
		}
		predictService.insertPredict(new Predict(base.truncatedTo(java.time.temporal.ChronoUnit.HOURS), requestTime,
				inputs, predictResults));
		for (int i = 0; i < 7; i++) {
			waterLevelService
					.insertWaterLevel(new WaterLevel(new WaterLevelId(id, waterHours[i]), waterLevels[i], target));
		}
	}
}

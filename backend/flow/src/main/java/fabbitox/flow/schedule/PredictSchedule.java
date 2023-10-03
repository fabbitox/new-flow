package fabbitox.flow.schedule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fabbitox.flow.dto.PredictInputs;
import fabbitox.flow.dto.PredictResult;
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
	private String[] xys;
	private String obscd;
	private int id;
	private String[][][] fcstValues;
	private Double[] waterLevels = new Double[7];
	private String waterStart;
	private LocalDateTime requestTime;
	private DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public PredictSchedule(int awsCount, String[] xys, String obscd, int id) {
		this.awsCount = awsCount;
		this.xys = xys;
		this.obscd = obscd;
		this.id = id;
		fcstValues = new String[awsCount][3][5];
	}

	@Scheduled(fixedRate = 200000)
	public void task() {
		requestTime = LocalDateTime.now();
		int minute = requestTime.getMinute();
		LocalDateTime base;
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
			int[] baseIndexes = new int[] {4, 8, 9, 2, 5};
			if (response.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode data = objectMapper.readTree(response.body());
				JsonNode item = data.get("response").get("body").get("items").get("item");
				for (int j = 0; j < 5; j++) {
					for (int k = 0; k < 3; k++) {
						fcstValues[i][k][j] = item.get(baseIndexes[j] * 6 + k).get("fcstValue").asText();
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
		    waterStart = data.get(startIndex).get("ymdh").asText();
		    for (int j = 0; j < 7; j++) {
				waterLevels[j] = data.get(startIndex + j).get("wl").asDouble();
			}
		    System.out.println();
		} else {
		    System.out.println("오류 응답: " + response.statusCode());
		    return;
		}
	}
	
	public void postPredict() {
		String url = flaskUrl + "/predict/" + id;
		WebClient webClient = WebClient.builder().baseUrl(url).build();
		Mono<String> responseMono = webClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new PredictInputs(fcstValues, waterLevels)))
                .retrieve()
                .bodyToMono(String.class);
		responseMono.subscribe(
                responseBody -> {
                	ObjectMapper om = new ObjectMapper();
                	try {
                		JsonNode jsonResult = om.readTree(responseBody);
                		Double[] results = new Double[3];
                		for (int i = 0; i < 3; i++) {
                			results[i] = jsonResult.get(0).get(i).asDouble();
                		}
						WebSocketHandler.sendData(om.writeValueAsString(new PredictResult(waterLevels, results, waterStart)));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
                },
                error -> System.err.println("Error: " + error.getMessage())
        );
	}
}

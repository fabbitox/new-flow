package fabbitox.flow.schedule;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fabbitox.flow.dto.PredictInputs;
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
	
	public PredictSchedule(int awsCount, String[] xys, String obscd, int id) {
		this.awsCount = awsCount;
		this.xys = xys;
		this.obscd = obscd;
		this.id = id;
	}

	@Scheduled(fixedRate = 200000)
	public void getData() {
		LocalDateTime requestTime = LocalDateTime.now();
		int minute = requestTime.getMinute();
		LocalDateTime base;
		if (minute < 45)
			base = requestTime.minusMinutes(minute + 30);
		else
			base = requestTime.minusMinutes(minute - 30);
		DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HHmm");
		String baseDate = base.format(ymd);
		String baseTime = base.format(hhmm);
		try {
			fcstValues = new String[awsCount][3][5];
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
            		waterLevels[j] = data.get(startIndex + j).get("wl").asDouble();
            	}
                System.out.println();
            } else {
                System.out.println("오류 응답: " + response.statusCode());
                return;
            }
            postPredict();
		} catch (Exception e) {
			System.out.println("오류 발생: " + e.getMessage());
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
                responseBody -> System.out.println("Response: " + responseBody),
                error -> System.err.println("Error: " + error.getMessage())
        );
	}
}

package fabbitox.flow.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
	private static Map<String, WebSocketSession> map = new HashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("user is connected: " + session.getId());
		map.put(session.getId(), session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("user is disconnected " + session.getId());
		map.remove(session.getId());
	}
	
	public static void sendData(String sendMessage) {
		Set<String> keys = map.keySet();
		System.out.println(String.format("message: %s, session count: %d", sendMessage, keys.size()));
		synchronized (map) { // 블럭안에 코드를 수행하는 동안 map 객체에 대한 다른 스레드의 접근 방지
			for (String key : keys) {
				WebSocketSession ws = map.get(key);
				try {
					ws.sendMessage(new TextMessage(sendMessage));
				} catch (IOException e) {
					
				}
			}
		}
	}
}

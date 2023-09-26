# 하천 수위 예측
## 데이터 수집
### 학습 데이터
기상자료개방포털 방재기상관측
https://data.kma.go.kr/data/grnd/selectAwsRltmList.do?pgmNo=56  
국가수자원관리종합정보시스템 수위자료
http://wamis.go.kr/wkw/wl_dubwlobs.do
### 실시간 예측 데이터
공공데이터포털 기상청 단기예보 조회
https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15084084  
국가수자원관리종합정보시스템 Open API 수위 시자료
http://www.wamis.go.kr:8080/wamisweb/wl/w8.do
## 입력
부산광역시 북구, 사상구, 사하구 기상 관측  
최근 7시간 낙동강 부산시(신평동) 수위
## 출력
1시간, 2시간, 3시간 후의 낙동강 부산시(신평동) 수위
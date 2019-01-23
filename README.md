# 데몬 프로세스 모니터링 시스템(Daemon process monitoring system)


배송 현황 추적 및 로그 수집 서버의 장애 상황 인지에 요구되는 리소스를 절약하고, 한눈에 운영 서버 현황을 확인하기 위한 웹 대시보드를 제공합니다.

Provides a Web dashboard to save the resources required to detect the status of delivery and log collection server.

## Getting Started

운영 서버의 로그 데이터 수집을 위해 logstash와 filebeat를 활용하였고, Spring boot로 서버를 구축하였습니다. 

I used logstash and filebeat to collect the log data of the delivery operating server, and built the local server with Spring boot.

### Prerequisites & Installing

* **Filebeat(경량 로그 수집기)**

    로그와 파일을 경량화된 방식으로 전달하고 중앙 집중화하여 작업을 보다 간편하게 만들어 주는 역할을 합니다.

    Logs and files are delivered in a lightweight fashion and are centralized to make the job easier.

    > Installation step
    > * [**Download**](https://www.elastic.co/kr/downloads/beats/filebeat) and unzip Filebeat 
    > * Edit the filebeat.yml configuration file
    > * Start the daemon by running sudo ./filebeat -e -c filebeat.yml
    > * Dive into the [**getting started guide**](https://www.elastic.co/guide/en/beats/filebeat/current/filebeat-getting-started.html) and [**video**](https://www.elastic.co/videos/getting-started-with-filebeat)
* **Logstash**
    
    Logstash는 실시간 파이프라인 기능을 가진 오픈소스 데이터 수집 엔진입니다. Logstash는 서로 다른 소스의 데이터를 탄력적으로 통합하고 사용자가 선택한 목적지로 데이터를 정규화할 수 있습니다. 다양한 고급 다운스트림 분석 및 시각화 활용 사례를 위해 모든 데이터를 정리하고 대중화(democratization)합니다.

    Logstash is an open source, server-side data processing pipeline that ingests data from a multitude of sources simultaneously, transforms it, and then sends it to your favorite “stash.”.

    > Installation step
    > * Check java -version is Java 8
    > * Download [**installation file**](https://www.elastic.co/downloads/logstash) and unzip
    > * Setting logstash.conf file
    > * Start the daemon by running ``` logstash -f logstash.conf  ```
    > * Dive into the [**getting started guide**](https://www.elastic.co/webinars/getting-started-logstash?baymax=default&elektra=docs&storm=top-video)


## Running the tests

end-end 흐름은 다음과 같습니다.
```
Filebeat - logstash - WAS(Spring) - MySQL
         /                |
Filebeat               browser
```
### Filebeat

로그 파일 변화를 감지하기 위해 설정된 yml파일을 실행시킵니다.

run filebeat.yml file to detect log files update.

> ./filebeat -e -c filebeat.yml
```
# filebeat.yml example

filebeat.inputs:
 - type : log
   enabled : true
   paths:
     - "your target path"

output.elasticsearch:
   hosts: ["ip:port"]
```

references : [**Filebeat yml guide**](https://www.elastic.co/guide/en/beats/filebeat/current/filebeat-configuration.html)

### Logstash

Filebeat로부터 수집된 데이터를 내재화하고 Spring 서버로 데이터를 전송합니다.

run logstash.conf file internalize filebeats data and send to Spring web server.

 >logstash -f logstash.conf 

```
# logstash.conf example

input{
    file{
        path => "your path"
        start_position => "beginning"
        ..
    }
}

filter{
    grok {
        match => { "message" => "%{COMBINEDAPACHELOG}" }
    }
    date {
        match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ] 
    }
}

output {
  elasticsearch { hosts => ["localhost:9200"] }
  stdout { codec => rubydebug }
}
```
references : [**Logstash config guide**](https://www.elastic.co/guide/en/logstash/current/config-examples.html)

### Spring sever business logic

1. Collect data & DB transaction

logstash.conf에 명시된 url로 내재화된 데이터를 포함하여 http 요청을 보냅니다. 아래는 해당 요청을 담당하는 Controller입니다.

```java
// MonitoringController.java

@RequestMapping(value = "/logging", method = RequestMethod.POST)
@ResponseBody
public LogRequest Logging(@RequestBody LogRequest logRequest){
    Daemon result = monitoringService.logging(logRequest); 
    if(result != null) {
        // notify to browser clients
        EchoHandler.notifyToUser(result);
    }
    return logRequest;
}
```

해당 요청을 처리하기 위한 WAS의 요청 처리 서비스(Service) 로직은 다음과 같습니다.

```java
/**
    * Logstash로부터 전송받은 로그 정보에 대해 에러 여부 판별 및 로그와 데몬에 대한 transaction 수행
    * @param logRequest 에러 여부를 판별한 로그 정보
    * @return 에러 존재 여부
    */
public Daemon logging(LogRequest logRequest) {
    // 1. 로그에 포함된 데몬 정보 추출
    //    Get the daemon information contained in the log

    // 2. LogRequest 객체의 timestamp를 통해 filebeat의 현재 시간 객체 생성
    //    Create the current time object of the filebeat through the timestamp of the LogRequest object
    
    // 3. Date 객체 생성 및 포맷 생성
    //    Create Date object and generate date format

    // 4. 오차 버퍼 지정 및 에러 판별
    //    Error buffer specification and error determination
    
    // 5. log에 ERROR 정보가 있을 경우 로그 추가
    //    Add log when ERROR information is in log
        
        // 5-1. 로그 추가 트랜잭션 ( ErrorType : INTERNAL )
        //      Log add transaction
        
        // 5-2. 정상 상태인 경우 상테 변경
        //      Change status

        // 5-3. 에러 로그 푸쉬
        //      Error log push
    
    // 6. log가 정상적으로 축적되었을 경우
    //    If log is accumulated normally
    
        // 6-1. 에러 상태인 경우 정상 동작
        //      Change status
        
        // 6-2. 데몬 정보 update 및 결과 지정
        //      Updating Daemon Information and Specifying Results
    
}
```

2. Moniotring process

DB에 축적된 데이터들을 모니터링 하기 위해 [크론식(Cron expression)](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm)을 스케줄 annotation(@Scheduled)을 활용하여 주기적으로 메소드를 호출시켜 DB 데이터를 모니터링 하였습니다.

```java 
// MonitoringService.java

@Scheduled(cron = "*/60 * * * * *")
public void monitoringScheduler() {
    // 1. 데몬 정보 추출 
    //    Load daemon information
    
    // 2. 각 데몬들의 정보에서 로그가 정상적으로 축적되었는지 확인 
    //    Check whether the logs are accumulated properly from the information of each daemon

    // 3. 현재 시간을 기준으로 Cron 식에 기반하여 데몬이 정상적으로 동작하였는지 체크 
    //    check whether the daemon is operating normally based on the Cron expression 
            
        // 3-1.에러가 발생하였을 경우 데몬 상태 변경, 에러 발생 및 로그 추가 트랜잭션
        //       If an error occurs, change the daemon status, generate an error, and add log transaction
        
    // 4. 데몬의 상태 변화가 있었을 경우
    //    If daemon status changes

        // 4-1. 웹 페이지 갱신 및 broadcast
        //        Update webpage & broadcast
}
```

## Deployment

이 프로젝트는 war 파일로 배포하였습니다.

This project was deployed by war file.

## Built With

* [Spring](https://spring.io/projects/spring-framework) - The web framework used
* [Filebeat](https://www.elastic.co/products/beats) - Log file monitoring used
* [Logstash](https://www.elastic.co/products/logstash) - Collect filebeats data and http communication used
* [Maven](https://maven.apache.org/) - Dependency Management
* [BootStrap](http://bootstrapk.com/) - The jsp dashboard page components used

## Authors

* **Dongik Park** 

Personal project.

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

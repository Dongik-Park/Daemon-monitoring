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

> logstash -f logstash.conf 
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

# hbase-test

## 0. 사전 필요 사항
* Apache HBase 2.2.3 (Standalone, 개발PC에 같이 설치)
* Apahe Hadoop 2.8.1 (Standalone, 개발PC에 같이 설치, 윈도우PC의 경우 WinUtils.exe 필요)
* 윈도우 환경변수 - 사용자변수에 다음 사항 추가 필요
  * HADOOP_HOME
  * HBASE_HOME
  * JAVA_HOME
* OpenJDK 11 필수

## 1. 테스트 실행 전 필수 사항
  * HBase Stanealone 실행
> start-hbase.cmd

  * HBase 테이블 생성 필요
> hbase shell
> 
> create 'test01', 'test_cf'
> 
> put 'test01', 'row1', 'test_cf:col1', 'value1'


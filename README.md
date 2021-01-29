java -jar file.agent-1.0.0.jar  
  
  
java option  
-Dserver.port={서버포트. 기본 9000}  
-Drequest.server.host={파일을 전송할 서버의 IP주소. 기본 localhost}  
-Drequest.server.port={파일을 전송할 서버의 Port번호. 기본 9000}  
  
   
Client에서 아래 명령어 입력시 파일 전송  
send {source} {target}  
예) send /data/test/client/test.txt /data/test/server/test2.txt   

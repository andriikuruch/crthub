curl -F "certificateFile=@<path/to/your/certificate.crt>;type=plain/text" http://localhost:8080/api/certificates/upload

curl -F "certificateFile=@C:\Users\andre\Downloads\legal-support.jetbrains.crt;type=plain/text" http://localhost:8080/api/certificates/upload

curl http://localhost:8080/api/certificates

curl http://localhost:8080/api/certificates/{id}

curl http://localhost:8080/api/certificates/0f9f5c95-41d6-4ec7-8c97-657e42a5db56

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar .\target\certhub-0.0.1-SNAPSHOT.jar


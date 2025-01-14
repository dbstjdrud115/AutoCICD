강의 주소(https://www.slog.gg/p/13960)


1.       1강은 깃허브 토큰을 생성했다.

	이 토큰은 도커에서 docker login ghcr.io -u USERNAME -p YOUR_TOKEN
	
	ghcr.io(?) 에 로그인할 때 쓰이는 듯 한데, 아무튼 도커와 git을 연결해주는것으로 보여진다. 


2.      2강부터 4강사이에, 우린 EC2에서 만들어준 가상서버(인스턴스) 내부에 Redis, Mysql, Nginx를 설치하고
	, git에서 받아온 코드를 실행하는 컨테이너도 설치했다.

	mysql설치 후, 설정과정에서 port:3306은 MySQL 데이터베이스의 기본 포트여서 그렇게 설정된것이며
	MySQL에는 기본적으로 root가 기본 userName이다.  비밀번호가 root123414인 이유는 아래 설정에 상응한다.

	 docker run 과정에 -p 3306:3306  (포트)
				  -e MYSQL_ROOT_PASSWORD=root123414 \ (비번 설정)
		

 
3. 5강은 강사님이 미리 만들어둔 Git Code에 대한 Image를 내려받으면서 Container를 만드는 과정이었다.

		
	mkdir -p /dockerProjects/blog/source
	cd /dockerProjects/blog/source
	git clone https://github.com/sik2/deploy-demo-01-25 .   => 폴더를 만들고(mkdir) 현재 위치에(.) Git 코드를 복붙했다. 

	vim src/main/resources/application-secret.yml  => application-secret.yml은 ignore대상임으로 secret을 만들고 세팅했다.

	docker build -t ll/blog .   => 현재 위치(.)의 코드를 도커로 이미지를 빌드했다. 이 위치에는 특히 도커 파일(Dockerfile)이라는게 있어, 거기에 설정이 적혀있다. 
						또한 우리에겐 terraform 파일들이 있는데 거기 설정에, git, java 등에 대한 각양 설정이 기입되어 있다. 
		
					     이미지란?
						애플리케이션이 실행되는 템플릿 또는 스냅샷 같은 것입니다.
						코드, 라이브러리, 설정, 의존성 등을 포함하며, 실행 가능한 상태지만 실제로 실행되지는 않습니다.

	docker run -d \
	  --name=blog_1 \    => 방금 만든 이미지를 기반으로 컨테이너 실행.


내가 구매한 도메인(api.blog.riskmgt.store) 에 접속이 가능해진다. 

한때는 도메인 접속이 안되길래,  run 시켜두고나서 build -t ll/blog .을 했는데, 하고 나니까 도메인 접속이 가능했다. 즉 실행해두고 나중에라도 이미지를 읽어들일 수 있는듯?


4. 6강은 만약 코드가 변경될 경우, 코드를 내려받고(git pull origin main)
	기존 이미지와 컨테이너를 삭제한 후(docker rm -f blog_1, docker rmi blog)

2강에서 컨테이너화한 mysql에서 사용할 계정을 만들고(create User root'@' ...)
docker exec -it mysql_1 mysql -u root -p
# 비번입력 : root123414
CREATE USER 'root'@'172.17.0.1' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'172.17.0.1'; # 권한부여
# REVOKE ALL PRIVILEGES ON *.* FROM 'root'@'172.17.0.1'; # 이 명령어는 알아두세요. root@172.17.0.1 사용자의 권한을 삭제하는 명령어 입니다. 
FLUSH PRIVILEGES;
exit	


아래와 같이 다시 실행해야 한다는 것을 의미한다. 
docker run -d \
  --name=blog_1 \
  --restart unless-stopped \
  -p 8081:8090 \
  -e TZ=Asia/Seoul \
  -v /dockerProjects/blog/volumes/gen:/gen \
  ll/blog:latest



5. 7강은 무중단 자동배포에 대한 설정이다. 

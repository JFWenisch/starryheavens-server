FROM java:8
WORKDIR /
ADD target/starryheavens-server*jar-with-dependencies.jar starryheavens-server.jar
EXPOSE 16000
CMD java -jar starryheavens-server.jar headless
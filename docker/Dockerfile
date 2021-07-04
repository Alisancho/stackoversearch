FROM openjdk:11
WORKDIR /app
EXPOSE 8080
COPY ../target/scala-2.13/stackoversearch-assembly-0.1.jar /app/stackoversearch-assembly-0.1.jar
CMD java -jar \/app\/stackoversearch-assembly-0.1.jar -server -Xmx2144 -XX:+UseParallelGC

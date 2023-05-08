#FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1
FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

RUN apt-get update

EXPOSE 8081

WORKDIR /io

ADD . /io

CMD sbt run
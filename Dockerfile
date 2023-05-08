FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0
#FROM checkers:0.5.0-SNAPSHOT
ENV DISPLAY=127.0.0.1

RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6 libgl1-mesa-glx libgtk-3-0

EXPOSE 8080

WORKDIR /CheckersInScala-SA

ADD . /CheckersInScala-SA

RUN chmod +x /CheckersInScala-SA/run.sh

CMD /bin/bash -c '/CheckersInScala-SA/run.sh; /bin/bash'

#CMD sbt run

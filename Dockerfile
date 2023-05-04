FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1

RUN apt-get update && apt-get install -y libxrender1 libxtst6 libxi6 libgl1-mesa-glx libgtk-3-0

EXPOSE 8080

WORKDIR /CheckersInScala-SA

ADD . /CheckersInScala-SA

RUN chmod +x /CheckersInScala-SA/run.sh

CMD /bin/bash -c '/CheckersInScala-SA/run.sh; /bin/bash'



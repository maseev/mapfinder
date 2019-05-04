FROM anapsix/alpine-java

RUN mkdir /opt/mapfinder

WORKDIR /opt/mapfinder
ADD ./target/mapfinder-1.0.0.RELEASE.jar /opt/mapfinder

EXPOSE 8080

CMD java -jar /opt/mapfinder/mapfinder-1.0.0.RELEASE.jar
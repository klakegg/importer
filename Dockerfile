FROM maven:3.6-jdk-8-slim AS maven

COPY . /src

RUN cd /src \
 && mvn -B --no-transfer-progress clean package \
 && cp -r target/$(ls target | grep "\-dist$" | head -1) /importer



FROM openjdk:8u212-jre-alpine3.9

COPY --from=maven /importer /importer

RUN echo "#!/bin/sh" > /bin/importer \
 && echo "sh /importer/bin/run.sh \$@" >> /bin/importer \
 && chmod a+x /bin/importer

VOLUME /src

WORKDIR /src

ENTRYPOINT ["importer"]

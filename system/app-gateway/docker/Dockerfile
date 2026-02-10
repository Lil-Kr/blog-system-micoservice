FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/openjdk:17

VOLUME /tmp
WORKDIR /

# 应用 Jar
ADD ./target/blog-gateway-1.0.0.jar app.jar

ENV JAVA_OPTS="\
-server \
-Xmx512m \
-Xms512m \
-Xmn256m"

ENTRYPOINT java \
${JAVA_OPTS} \
-Djava.security.egd=file:/dev/./urandom \
--add-opens=java.base/java.lang=ALL-UNNAMED \
--add-opens=java.base/java.io=ALL-UNNAMED \
--add-opens=java.base/java.util=ALL-UNNAMED \
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED \
--add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED \
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens=java.base/java.util=ALL-UNNAMED \
--add-opens=java.base/java.math=ALL-UNNAMED \
-jar app.jar
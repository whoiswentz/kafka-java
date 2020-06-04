CFLAGS=-I/usr/local/opt/openssl/include LDFLAGS=-L/usr/local/opt/openssl/lib npm install --save node-rdkafka

bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --list --bootstrap-server localhost:9092

kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ECOMMERCE_NEW_ORDER

kafka-console-producer --broker-list localhost:9092 —topic ECOMMERCE_NEW_ORDER

kafka-console-consumer --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --from-beginning# kafka-java

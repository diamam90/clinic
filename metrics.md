docker run --name victoria-metrics -p 8428:8428 -v ./config/victoria-metrics/promscrape.yaml:/promscrape.yaml victoriametrics/victoria-metrics:v1.93.12 -promscrape.config=promscrape.yaml

docker run --name grafana -p 3000:3000 -v ./data/grafana:/var/lib/grafana -u "$(id -u)" grafana/grafana:10.2.4

docker run --name zipkin -p 9411:9411 openzipkin/zipkin:3.5.1
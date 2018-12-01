IMAGE_NAME="apacheignite/ignite"
IGNITE_VERSION="2.6.0"
CONTAINER_NAME="apache-ignite"

docker stop ${CONTAINER_NAME}
docker run --rm -d --name ${CONTAINER_NAME} -p 11211:11211 -p 47100:47100 -p 47500:47500 -p 49112:49112 ${IMAGE_NAME}:${IGNITE_VERSION}
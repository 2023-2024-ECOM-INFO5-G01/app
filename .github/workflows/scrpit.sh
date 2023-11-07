cd app/
docker container stop jhipster
git pull
docker container start jhipster
docker exec jhipster /bin/sh -c "./gradlew &"

# Проект ПУЖЦ

```
./gradlew clean bootJar -x test
docker build . -t softline/suvv:0.4.4
docker save docker.io/softline/suvv:0.4.4 > ./build/suvv-0.4.4.tar
```
```
docker load < suvv-0.4.4.tar
docker rm -f suvv
docker run --env-file .env -p 8080:8080 -v ~/jmix:/.jmix --name suvv softline/suvv:0.4.4
```
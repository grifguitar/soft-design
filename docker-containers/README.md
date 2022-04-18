# docker-containers

Выполнил: студент группы М34371, Хлытин Григорий.

## results:

```haskell
gradle >> docker-containers:khlyting-server [shadowJar]
|
$ cd khlyting-server
|
$ docker build . -t khlyting-server
|
| Sending build context to Docker daemon   14.9MB
| Step 1/4 : FROM openjdk:8
|  ---> 18fbe41f975e
| Step 2/4 : EXPOSE 8080
|  ---> Using cache
|  ---> 9edf244cf595
| Step 3/4 : ADD /build/libs/khlyting-server-1.0-SNAPSHOT-all.jar khlyting-server.jar
|  ---> 2d6dd607d922
| Step 4/4 : ENTRYPOINT ["java", "-jar", "khlyting-server.jar"]
|  ---> Running in 8f6f21fdf48b
| Removing intermediate container 8f6f21fdf48b
|  ---> 63a9d7faade8
| Successfully built 63a9d7faade8
| Successfully tagged khlyting-server:latest
|
$ docker run -p 8080:8080 khlyting-server
|
| [main] INFO ktor.application - Autoreload is disabled because the development mode is off.
| [main] INFO ktor.application - Responding at http://0.0.0.0:8080
| [main] INFO ktor.application - Application started in 0.091 seconds.
|
```

![image.png](https://github.com/grifguitar/soft-design/blob/main/docker-containers/img.png)

# RPL-2.0

This service has the producer backend and the message broker.
You can also add a MySQL server to persist data. If not, an H2 in-memory database is used (so as soon 
as you close the JVM, all data is lost) 

## Endpoints

A `swagger.json` is generated every time functional tests are run.
To see them in a nice UI you can copy the file to [Swagger Editor](https://editor-next.swagger.io/).

---

## Bootstrapping

### Local with minikube cluster + MySQL within independent container

- In order to build a local docker image to provide within the minikube cluster later, first we need to build the app via gradle. As the gradle version is outdated, it is considerably easier to use a base image directly:

```shell script
docker run --rm -v "$(pwd)":/workspace -w /workspace gradle:6.8-jdk11 ./gradlew build
```

- Now we can use the `Dockerfile` to build the image:

```shell script
docker build -t producer:local .
```

- Next, edit the `kubernetes/deployment/producer.yaml` file to use the `producer:local` image instead of the one from cloud registry.

- Now we can start the minikube cluster and load the image into it:

```shell script
minikube start
minikube image load producer:local
```

- Create the message broker deployment and service:

```shell script
kubectl create -f ./kubernetes/deployments/queue.yaml
kubectl create -f ./kubernetes/services/queue.yaml
```

- Before the next step, as the producer deployment sets `SPRING_PROFILES_ACTIVE`, we need to create the respective secrets (this only needs to be done once):

> [!NOTE]  
> As we will use a MySQL image on a container that's separate from the cluster, we have to inform the cluster of the ip where the DB server is located. To do so, we reference the name that minikube uses to address the host machine (not the docker gateway but the host itself. See: [minikube handbook](https://minikube.sigs.k8s.io/docs/handbook/host-access/)). This way we can simply expose the MySQL container to the host machine via the compose file port mapping (If the connection to the db is still not working, access the mysql shell to check if we can connect from any host)

```shell script
kubectl create secret generic jwt-secret --from-literal=secret=<REPLACE_WITH_YOUR_SECRET>;
kubectl create secret generic db-host --from-literal=host=host.minikube.internal;
kubectl create secret generic db-username --from-literal=username=<REPLACE_WITH_YOUR_VALUE>;
kubectl create secret generic db-password --from-literal=password=<REPLACE_WITH_YOUR_VALUE>;
kubectl create secret generic db-name --from-literal=name=<REPLACE_WITH_YOUR_VALUE>;
kubectl create secret generic rpl-help-email --from-literal=email=<REPLACE_WITH_YOUR_VALUE>;
kubectl create secret generic rpl-help-password --from-literal=password=<REPLACE_WITH_YOUR_VALUE>;
```

- Create the producer deployments and services:

```shell script
kubectl create -f ./kubernetes/deployments/producer.yaml
kubectl create -f ./kubernetes/services/producer.yaml
```

- Get the ip address of the cluster:

```shell script
minikube ip
```

- Optional: Open minikube dashboard

```shell script
minikube dashboard
```

- Optional: View the broker dasboard at `http://<cluster_ip>:31157`.



#!/bin/sh

kubectl create secret generic jwt-secret --from-literal=secret=sarasa;
kubectl create secret generic db-host --from-literal=host=host.minikube.internal;
kubectl create secret generic db-username --from-literal=username=root;
kubectl create secret generic db-password --from-literal=password=rootpassword;
kubectl create secret generic db-name --from-literal=name=rpl;
kubectl create secret generic rpl-help-email --from-literal=email=mvasquezj@fi.uba.ar;
kubectl create secret generic rpl-help-password --from-literal=password=sarasa;



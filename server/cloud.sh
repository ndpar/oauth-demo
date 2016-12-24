#!/bin/sh

mvn clean package
cf push --hostname oauth-server
#cf logs oauth-server

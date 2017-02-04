#!/usr/bin/env bash
scp -P 6992 ./target/lexy_auth.war tomcat@218.4.150.106:/home/tomcat/prod/server/apache-tomcat-7.0.64/webapps/

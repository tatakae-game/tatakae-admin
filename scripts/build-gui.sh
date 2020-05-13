#!/bin/bash

cd core
mvn clean install
cd ..

cd gui
mvn clean package
cd ..

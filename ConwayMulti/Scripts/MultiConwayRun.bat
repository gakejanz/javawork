@echo off

cd ..
cd src
javadoc -d MultiConwayDocs ganzj2.hw04
javac ganzj2/hw04/MultiConwayMain.java
java -Xms2g -Xmx7g ganzj2/hw04/MultiConwayMain
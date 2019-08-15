@ECHO OFF
cd ..
cd src
javadoc -d BattleshipDocs hw03/ganzj2/*.java
javac hw03/ganzj2/BattleshipMain.java
java hw03/ganzj2/BattleshipMain

@echo off
echo Starting Simple Horoscope Server...
echo.

REM Create directories if they don't exist
if not exist "target\classes" mkdir target\classes

REM Compile the simple server
echo Compiling SimpleHoroscopeServer...
javac -encoding UTF-8 -d target/classes src/main/java/com/example/horoscope/SimpleHoroscopeServer.java

REM Run the simple server
echo Starting server on http://localhost:8080...
java -cp "target/classes" com.example.horoscope.SimpleHoroscopeServer

pause
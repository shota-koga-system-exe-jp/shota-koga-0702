@echo off
echo Starting Horoscope Service...
echo.

REM Create directories if they don't exist
if not exist "target\classes" mkdir target\classes

REM Compile Java files
echo Compiling Java files...
javac -encoding UTF-8 -d target/classes -cp "src/main/java" src/main/java/com/example/horoscope/*.java src/main/java/com/example/horoscope/model/*.java src/main/java/com/example/horoscope/service/*.java src/main/java/com/example/horoscope/controller/*.java

REM Copy resources
echo Copying resources...
xcopy /s /y src\main\resources\* target\classes\

REM Run the application
echo Starting application...
java -cp "target/classes" com.example.horoscope.HoroscopeServiceApplication

pause
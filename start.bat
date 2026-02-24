@echo off
java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -cp target/classes com.example.App
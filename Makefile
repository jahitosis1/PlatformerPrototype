all:
	javac --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls -d bin src/*/*.java
	java --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls -cp bin main.Main

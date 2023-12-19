all:
	javac --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls,javafx.media -d bin src/*/*.java
	java --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls,javafx.media -cp bin main.Main

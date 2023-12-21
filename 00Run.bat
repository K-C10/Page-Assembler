Title Compilling...
javac *.java

jar -0 --create --file PageAssembler.jar --main-class=MainGUI *.class *.png

Title Running...

del *.class

java -jar PageAssembler.jar
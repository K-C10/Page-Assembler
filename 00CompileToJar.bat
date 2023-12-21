Title Compilling...
javac *.java

jar -0 --create --file PageAssembler.jar --main-class=MainGUI *.class *.png

del *.class

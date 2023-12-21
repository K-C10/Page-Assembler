mkdir TEMP
cd code
javac *.java

mv ./*.class ./../TEMP
cd ..
cp ./assets/* ./TEMP

cd TEMP

jar -0 --create --file ./../PageAssembler.jar --main-class=MainGUI *.class *.png

cd ..

rm -rf ./TEMP
echo build done!

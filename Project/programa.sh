#!/bin/bash

cd FONTS
make presc

cd ../EXE
java -cp :../FONTS/lib/*: main.Presentation.CtrlPresentacio


cd ../FONTS
make clean
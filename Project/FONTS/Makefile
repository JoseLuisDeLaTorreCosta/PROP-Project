CLASS_MAIN = main/Domain
CLASS_OUTPUT = ../EXE
CLASS_INPUT = ./$(CLASS_MAIN)/*.java ./$(CLASS_MAIN)/Hill_Climbing/*.java ./$(CLASS_MAIN)/QAP/*.java
CLASS_TESTS = ./test/domain/*.java ./test/domain/Alfabet*.java 
JUNIT_JARS = ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar

all: 
	javac -d $(CLASS_OUTPUT) $(CLASS_INPUT)

drivers: Driver_HC Driver_QAP

Driver_HC:
	javac -d $(CLASS_OUTPUT) drivers/Driver_HC.java

Driver_QAP:
	javac -d $(CLASS_OUTPUT) drivers/Driver_QAP.java

files:
	javac -d $(CLASS_OUTPUT) main/Persistence/Files.java

pc:
	javac -d $(CLASS_OUTPUT) -cp ./lib/json-20231013.jar:. main/Persistence/Persistence_Controller.java

dc:
	javac -d $(CLASS_OUTPUT) -cp ./lib/json-20231013.jar:. main/Domain/CtrlDomini.java

presc:
	javac -d $(CLASS_OUTPUT) -cp ./lib/json-20231013.jar:. main/Presentation/CtrlPresentacio.java

tests: test_Alfabet test_Bounding test_HeuristicFunction test_HungarianAlgorithm test_iniGreedy test_Llista_Paraules test_MinimalLines test_Paraula test_QAP test_ReturnArray test_State

test_Alfabet:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/Alfabet/Alfabet_Test.java

test_Bounding:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/Bounding/BoundingTest.java

test_HeuristicFunction:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/HeuristicFunction/Heuristic_Test.java

test_HungarianAlgorithm:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/HungarianAlgorithm/HungarianAlgorithmTest.java

test_iniGreedy:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/iniGreedy/iniGreedyTest.java

test_Llista_Paraules:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/Llista_Paraules/Llista_Paraules_Test.java

test_MinimalLines:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/MinimalLines/*.java

test_Paraula:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/Paraula/Paraula_Test.java

test_QAP:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/QAP/QAP_Test.java

test_ReturnArray:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/ReturnArray/ReturnArray_Test.java

test_State:
	javac -d $(CLASS_OUTPUT) -cp $(JUNIT_JARS):. test/domain/State/State.java

clean:
	rm -r $(CLASS_OUTPUT)/*
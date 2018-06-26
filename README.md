# project2exam

## Question 6
1. For external jars, you should importjackson-annotations-2.8.3.jar, jackson-core-2.8.3.jar, jackson-databind-2.8.3.jar, jsoup-1.10.1.jar.

2. For running the code, you should open the project in Intellij and then run the main function. The result will be sored in result.txt.

## Question 7
1. Run the code
```bash
./bin/spark-submit --master local[8] --driver-memory 2G avgbid.py
```
The result will be stored in avgbidresult folder.

2. Run the code
```bash
./bin/spark-submit --master local[8] --driver-memory 2G ratio.py
```
The result will be stored in ratioresult folder.

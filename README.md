# Assignment:	Package	Challenge
Package	Challenge project

## Introduction

You want to send your friend a package with different things.Each thing you put inside the package has such parameters as index number, weight and cost. The package has a weight limit. Your goal is to determine which things to put into the package so that the total weight is less than or equal to the package limit and the total cost is as large as possible. 

#Input sample file:

API should accept as its first argument a path to a filename. The input file contains several lines.
Each line is one test case. 

 81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
 8  : (1,15.3,€34)
 75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
 56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)

# Output sample:

For each set of items that you put into a package provide a new row in the output string (items’ index
numbers are separated by comma). E.g.

 4
 -
 2,7
 8,9

# Prerequisites:
a.Java 8
b.Maven

# Running the project:
a.In root folder, do:<pre>mvn clean install</pre>
b.After successful build, execute <pre>mvn spring-boot:run</pre>
c.Server Port Configured <pre>8082</pre>

# Design Overview:
1. Separation of concern has been used for designing the layered architecture.
2. All the classes with similar concerns have been grouped under same package forming an layered architecture.
3. All the classes have single responsibility and all the classes which had multiple responsibility for been separated accordingly.
4. Interface has been created and can be segregated in future to add more functionality through implementation classes.
5. Inversion of control(DI) has been used so that all the layers are loosely coupled so that implementation classes can be easily changed in future if required & also easy  to write test cases.
6. While implementing this functionality Test Driven Approach has been followed.
7. Integration Test and Unit test has been implemented.

# Implementation Overview:
This application exposes below end point url

    POST:/getPackage	
    This end point returns the indexes of the items which can be put in each package.
    Example : http://localhost:8082/getPackage
    Request :       {
 	                      "filePath": "C:\\Users\\C62975\\Files\\Game\\example_input"
                          }
                   
    Response for Success scenario :   {
    "indexes": [
        "4",
        "-",
        "2,7",
        "8,9"
    ]
    }
    
   
                   



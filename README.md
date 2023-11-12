# Concurrency 
## Contents
* [About the assignment](#about-the-assignment)
* [Prerequisites](#prerequisites)
* [Getting Started](#getting-started)
* [Usage](#how-to-run-the-application)
* [Written by](#author)


## About the assignment
The task includes image processing and usage of multiple threads in order to pixelize the provided image. The application has 2 modes 'S' and 'M', single and multi-threading, respectively. The pixelization is done in 2 modes according to the running mode:

- Single thread: Left to right, top to bottom
- Multi thread: Division into n strips (n being number of availible threads), and processing from left to right


## Prerequisites
All the codes in this repository is written in  Java programming language. In order to use the application, Java should be installed in the system. Following the steps and is necessary for running the codes.

#### Installing Java

For running codes written in Java, Java Development Kit should be installed.

[https://www.oracle.com/java/technologies/downloads/#jdk21-windows](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)

After installation, open the terminal and run the command *"javac -version"*, If you see the Java version, you are good to continue.


## Getting Started
In this section, all the necessary steps to run the applications are highlighted.

First of all, we will need the codes to be downloaded to our system. If you have git, you can directly clone the repository by:
```
git clone https://github.com/ADA-GWU/3-concurrency-DDursun.git

```

You can also download all the codes from Github as a Zip file.


## How to run the application
 
1. Open the corresponding folder path in your terminal and compile Java files using:

```bash

javac Main.java

```
Input format should be: java Main.java "filename" "square size" "processing mode"

```bash

java Main test1.jpg 20 M

```



## Written by
Dursun Dashdamirov
CSDA master's student.



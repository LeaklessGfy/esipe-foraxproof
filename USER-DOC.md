# User documentation
## What is Foraxproof ?
Foraxproof is a code analyzer developed with several objectives:
* Make the developer code great again and cleaner.
* To force the respect of the good practices.
* Helping new and experienced developers get better with automatic code reviews.

## How it works ?
The foraxproof version developed by the APVRProject team has been designed to be both easy to use in its classic version but also highly configurable for users who need to perform more refined analyzes.
The program itself operates using a system of modules, allowing each company and each user to develop specific plugins to perform code analysis without the need to recompile the developed modules nor the core part from the program. In other words, just **develop** your new module, **compile** it and **inject** it at runtime for it to be taken into account by foraxproof.

## Ok it's cool but how to use it ?
### Use the very lastest version
In this part, we will explain how to get the very latest version from the source code, thus going through the compilation step.
1. Install Maven if you don't have it `sudo apt-get install maven2`
2. Check if Maven use the JDK9 with `mvn -v`
3. If Maven does not use JDK9, modify your java home :
*  For example, on UPEM computers : `export JAVA_HOME=/usr/local/apps/alternatives/java/jdk-9+181` and next `export PATH=${PATH}:$JAVA_HOME/bin`
4. Execute `mvn clean package` to create JARS
5. Verify if all needed jar are in lib folder and you are ready to go !

### Execute
**You are on Linux, use this magic command (don't forget to add parameters) :**
```
java -p lib/foraxproof.impl-1.0.jar:lib/foraxproof.core-1.0.jar:lib/asm-6.0.jar \
     --add-modules fr.upem.foraxproof.impl \
     -jar lib/foraxproof.main-1.0-jar-with-dependencies.jar \
      $@
```
**You are on Windows, use this base command instead :**
```
java -p lib/foraxproof.impl-1.0.jar;lib/foraxproof.core-1.0.jar;lib/asm-6.0.jar ^
     --add-modules fr.upem.foraxproof.impl ^
     -jar lib/foraxproof.main-1.0-jar-with-dependencies.jar ^
      %*
```
or simply run : `run.sh or run.cmd`

## Options availables with Foraxproof
### Required options
| Option |  Description |
|---	|---	|
| `-j <PATH_TO_JARFILE>` | Specify a JAR file. Parses all classes in the JAR. (*) |
| `-c <CLASS_PATH1>,<CLASS_PATH2>,<CLASS_PATH3>...` | Specify a class to analyse with the program. You can use multiple rules separated by ','. (*) |
| `-d <PATH_TO_DIRECTORY>` | Specify a directory. Parses all classes, recursively, in the directory. (*) |  
| `-u <URL>` | Specify an url of jar to analyse. (*) |

(*) You need to use ONE of the four options with this note.

### Optional options
| Option | Description |
|---	|---	|
| `-r <RULE_NAME1>,<RULE_NAME2>,<RULE_NAME3>...` | Specify a rule to use. You can use multiple rules separated by ','. If does not specified, all rules availables are used. |
| `-s` | Option used to launch the SpringBoot server. |
| `-m <MODE_NAME>` | Specify the mode to use ['concurrent' or 'default'] |
| `-t <NB_THREAD>` | Specify the number of thread to use. Only available in concurrent mode. |
| `-z <PATH_TO_SOURCE_ZIP>` | (Not ready) Specify a source ZIP file. Allow to have source extract inside vuejs. |
| `-e <EXPORTER_NAME1>,<EXPORTER_NAME2>,<EXPORTER_NAME3>...` | Specify the exporter to use ['xml', 'sql' or 'print']. You need to use sql exporter if you want to show exports in VueJS app. You can use multiple exporter separated by ','. |
| `-h` | Display help message. |


### Examples
* `-u http://central.maven.org/maven2/org/apache/commons/commons-collections4/4.1/commons-collections4-4.1.jar -e xml`
* `-u http://central.maven.org/maven2/org/apache/commons/commons-collections4/4.1/commons-collections4-4.1.jar -s -e sql -r LengthRule`
*  `-j resources/samples-rt.jar -s -e sql -m concurrent`
* `-d . -e sql -m concurrent -s` -> To analyse the project with Foraxproof

## Use the VueJS app
To use the VueJS app, just do an analysis with foraxproof and add the option -s to launch REST SpringBoot server. When it is done, you just need to open www/src/index.html file in your favorite browser.
The REST app can only be consulted with HTTPS connection, you need to add the certificate located in `fr.upem.foraxproof.rest/src/main/resources` in your browser. The password is `123456a`.


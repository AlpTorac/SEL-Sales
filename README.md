# Introduction

SEL-Sales was a Java project I have worked on alone in my free time to improve my programming skills. As of writing this, it is the second largest software project I have worked on, the first one being [GELF](https://github.com/AlpTorac/PSE-Report), where I have worked with 4 other collaborators.

This project's purpose was to help cafés/restaurants manage menus, take orders and to save them in a centralised server. To this end, the personnel would use the client application and connect it to the server application via bluetooth connections. Bluetooth connections are established with the help of [BlueCove](https://github.com/hcarver/bluecove/tree/master) library and the UI is implemented with [JavaFX](https://openjfx.io).

The Model-View-Controller architecture was used as reference during the implementation for both clients and servers (components). One of the deviations from the said architecture was the addition of an "External" element, which represents the clients in the server-part and the other way around. The external element is essencially a variation of the View-part of the architecture, which is dedicated to communicate external components. This way, the local and remote concerns are kept separated and the rest of the application works the same for UI inputs and remote inputs.

The project was eventually abandoned, as I did not have the time for it any more and still do not have. In the end, I have decided to make this repository public as a code sample.

# Specifications

The project was last compiled with Java 12 compliance and requires adding JavaFX modules as virtual machine arguments before launch. More information regarding the used libraries can be found in the ".pom" files used by [Maven](https://maven.apache.org).

Test cases require [JUnit 5](https://junit.org/junit5/) and [TestFX](https://github.com/TestFX/TestFX) dependencies to run, which are handled by Maven. Some test cases have been disabled due to them taking too long and/or yielding inconsistent results. Since there are also UI tests in test suites, running them will open windows in quick succession and perform various UI operations on them.

# Problems

I have started and worked on this project to experiment with Java and some design decisions that I have had. That is to say, this project was improvised and has NOT been developed with modern technologies (such as [The Spring Framework for Java](https://spring.io) or similar). Congruently, this project has many problems, some of which I have listed below.

## No documentation

Since this project is improvised, there was little to no planning in its making, which has lead the functionality to constantly keep changing. Also, I wanted to work on it alone without ever making it public at the time. Consequently, for the sake of focusing on practicing coding at the time, I did not document the project properly in its making.

## Overly complicated

One of my main goals in this project was to practice object-orientation i.e. polymorphism and some design patterns in Java. With that said, I did not hesitate to make more classes/interfaces/packages than I needed to, which has made things considerably more complicated than they needed to be.

## Bad Test Implementation

At the time, I was not aware of the possibility of polymorphism in JUnit tests, which help spare large amounts of code repetition and would have left test cases significantly tidier. As a result, many attributes were repeated across multiple JUnit test cases unnecessarily.

## Deadlocks in Connectivity

I have initially programmed the "External"-part of the project in a way that each separate connection is handled by a separate thread. Unbeknown to me at the time, that was bad practice and has often caused deadlocks while trying to test connecting multiple dummy clients with a dummy server, hence the multiple client tests being disabled and some other client tests being commented out. Due to the lack of time, I could not fix it.

A possible fix coult be having 1 thread continuously look for and accept incoming connections; 1 thread continuously read from them and add incoming requests to a queue; 1 thread take and compute the said requests from that queue and add their result to another queue; 1 thread send the aforementioned results back. This opposed to having a separate thread for each connection would help circumvent deadlocks.

## Subobtimal Data Persistence

No modern databank or persistence technologies were used in this project. As a result, storing and transferring data within the project is complicated and error-prone. To handle data inconsistencies between the server and clients, data exchanged between the server and the clients (order related data for instance) is simply written to .txt files and new data causes the whole data to be deleted and re-written.

Using such databank or persistence technologies in conjunction with the means to access them would make storing and manipulating data a lot easier and help the clients access the data on the server concurrently.

# The Future of This Project

As implied by above, this project has severe issues that cannot be reasonably fixed without a full rework. That is to say, it would have to be started from scratch and be developed with modern technologies, if it is to be used in the real world. All in all, this project was a learning experience and will not be continued for the foreseeable future.

# Disclaimer

!!! This is an improvised and incomplete project, which cannot be used reliably in the real world as it currently is. It also does not secure the established connections or the stored data by any means !!!

The code given here may not satisfy any widespread coding standards and contains (very) questionable design decisions, as it was a learning experience. This is also the reason why most of the functionality is implemented by me, even though it could have been possible to use open-source external libraries.

The whole implementation process was done on Windows (prior to Windows 11) using the Eclipse IDE and the project has not been run on any other operating system. Besides the automated unit tests, there were also manual tests, which involved connecting only 1 real server with 1 real client. So the implementation has not been tested for multiple real clients, hence the high likelihood of bugs while attempting to connect 1 server to N clients. Moreover, the connectivity in automated tests is simulated by connecting dummy input and output streams, as testing the connectivity based on real bluetooth connections was not possible.

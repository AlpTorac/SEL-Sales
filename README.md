# Introduction

SEL-Sales was a Java project I have worked on alone in my free time to improve my programming skills.

This project's purpose was to help cafés/restaurants manage menus, take orders and to save them in a centralised server. To this end, the personnel would use the client application and connect it to the server application via bluetooth connections. Bluetooth connections are established with the help of BlueCove library and the UI is implemented with JavaFX.

The Model-View-Controller architecture was used as reference during the implementation for both clients and servers (components). One of the deviations from the said architecture was the addition of an "External" element, which represents the clients in the server-part and the other way around. The external element is essencially a variation of the View-part of the architecture, which is dedicated to communicating external components. This way, the local and remote concerns are kept separated and the rest of the application works the same for UI inputs and remote inputs.

The project was eventually abandoned, as I did not have the time for it any more.

# Specifications

The project was last compiled with Java 12 compliance and requires adding the JavaFX modules as virtual machine arguments before launch. More information regarding the used libraries can be found in the ".pom" files used by Maven.

Test cases require JUnit 5 and TestFX dependencies to run, which are handled by Maven. Some test cases have been disabled due to them taking too long and/or yielding inconsistent results. Since there are also UI tests in test suites, running them will open windows in quick succession and perform various UI operations on them.

# Disclaimer

!!! This is an improvised and incomplete project, which cannot be used reliably in the real world as it currently is !!!

The code given here may not satisfy any widespread coding standards and may contain questionable design decisions, as it was a learning experience. This is also the reason why most of the functionality is implemented by me, even though it could have been possible to use open-source external libraries.

The whole implementation process was done on Windows (prior to Windows 11) using the Eclipse IDE and the project has not been run on any other operating system. Besides the automated unit tests, there were also manual tests, which involved connecting only 1 server with 1 client. So the implementation has not been tested for multiple clients, hence the high likelihood of bugs while attempting to connect 1 server to N clients. Moreover, the connectivity in automated tests is simulated by connecting dummy input and output streams, as testing the connectivity based on real bluetooth connections was not possible.

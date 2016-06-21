Dear Stephan & Patrick,

1) To execute gradlew with "gradlew createDebugCoverageReport" to proof the code coverage, 
you may have to set the permissions manually, i.e. while the tests are executed you have
to click "allow" if the app asks for a permission.
!!!Then you have to run the command once again!!! (See Point 4 for more details!)

2) It's quite difficult to set new locations in unit tests. Since we have a navigation-app
this is necessary. If you use the version of AndroidStudio, which was current at the 
beginning of the course, everything works fine. Newer versions of AndroidStudio and 
Emulators can cause problems. Unfortunately we weren't able to find a fix for the newer
versions. (During the execution of a test a connection to telnet cannot be established. However, 
this is necessary to test the navigation feature of our app)

3) For testing the navigation we use "geo fix". Therefore, we build a socket and try to send the
geo fix command over a telnet protocol. However, this is not a very reliable way and sometimes
the socket is not fast enough. If the app wait endlessly during executing the test, please abort
the run and restart the test case.

4) For executing the test we have to enter decimal numbers into text fields. The convention
for the comma is different in German and English (. in English and , in German). As the lecture
is held in English we agreed to use the English version. Therefore you need an English Emulator
to execute the tests

5) The tests need to be executed with Android Version >= 23. Robotium is not ready for
testing runtime permissions so we had to find workarounds. 
(https://github.com/RobotiumTech/robotium/issues/816#issuecomment-215187029)

6) Since we use a free weather api, it is not 100 % reliable. For this reason it can happen
that we do not get the current weather. This leads to lower code coverage. We included a test report
on our team page to show the full coverage. 

Cheers
Andrea, Angela, Clemens, Gerald

                                       ___-------___
                                   _-~~             ~~-_
                                _-~                    /~-_
             /^\__/^\         /~  \                   /    \
           /|  O|| O|        /      \_______________/        \
          | |___||__|      /       /                \          \
          |          \    /      /                    \          \
          |   (_______) /______/                        \_________ \
          |         / /         \                      /            \
           \         \^\         \                  /               \     /
             \         ||           \______________/      _-_       //\__//
               \       ||------_-~~-_ ------------- \ --/~   ~\    || __/
                 ~-----||====/~     |==================|       |/~~~~~
                  (_(__/  ./     /                    \_\      \.
                         (_(___/                         \_____)_)
(c) http://www.asciiworld.com/-Animals-.html


Dear Stephan & Patrick,

1) To execute gradlew with "gradlew createDebugCoverageReport" to proof the code coverage, 
you may have to set the permissions manually, i.e. while the tests are executed you have
to click "allow" if the app asks for a permission.
Then you have to run the command once again.

2) It's quite difficult to set new locations in unit tests. Since we have a navigation-app
this is necessary. If you use the version of AndroidStudio, which was current at the 
beginning of the course, everything works fine. Newer versions of AndroidStudio and 
Emulators can cause problems. Unfortunately we weren't able to find a fix for the newer
versions. (During the execution of a test a connection to telnet cannot be established. However, 
this is necessary to test the navigation feature of our app)

3) The tests need to be executed with Android Version >= 23. Robotium is not ready for
testing runtime permissions so we had to find workarounds. 
(https://github.com/RobotiumTech/robotium/issues/816#issuecomment-215187029)

4) Since we use a free weather api, it is not 100 % reliable. For this reason it can happen
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


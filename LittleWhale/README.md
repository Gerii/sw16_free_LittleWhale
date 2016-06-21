Dear Stephan & Patrick,

1) to execute gradlew with "gradlew createDebugCoverageReport" to proof the code coverage, 
you may have to set the permissions manually, i.e. while the tests are executed you have
to click "allow" if the app asks for a permission.
Then you have to run the command once more.

2) it's quite difficult to set new locations in unit tests. Since we have a navigation-app
this is necessary. If you use the version of AndroidStudio, which was current at the 
beginning of the course, works fine. Newer versions of AndroidStudio and Emulators can
cause problems. Unfortunatly we weren't able to find a fix for the newer versions.

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


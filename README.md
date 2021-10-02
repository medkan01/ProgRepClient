# ProgRepClient

To execute this project you will need the other part named [ProgRepServer](https://github.com/medkan01/ProgRepServer).

When lauching the main class, you will need to specify arguments : 
- the name of the host : in our case we will stay in local so `localhost`
- the port on wich you want to run the client : you have to put the **SAME** port as the server

If you are running this on eclipse put this in VM arguments : `--module-path \"lib\\javafx-sdk-11.0.2\\lib\" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web`.
This parameter is accessible as this : 
- right click on the name of the project
- go on `Run As` -> `Run Configurations...`
- go to pannel `Arguments`

If you are running this program on Visual Studio Code, you must have your configuration ready to run Java (install this [extension](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) from vs code ) programs then : 
- open the folder where you have the program
- create a launch.json file where you will put this :
``` json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "Launch Client",
            "request": "launch",
            "mainClass": "client.Client",
            "projectName": "ProgRepClient",
            "args": [
                "localhost",
                "3000"
            ],
            "vmArgs": "--module-path \"lib\\javafx-sdk-11.0.2\\lib\" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web"
        }
    ]
}
```
- right click on `Serveur.java` and click `Run Java`

If you want to open an other window of client do as following : 
- a side bar next to the console will be created with atleast 1 element : `Java Process Console`. Click on it and click on the icon to split (on the right of the name) the terminal ( Ctrl + Shift + ( )
- press the `up arrow` key on your keyboard to get the last command executed and press `Enter`

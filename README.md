# medmatch

## Running the app on Eclipse

1. Clone the project into the Eclipse ([see how](https://github.com/collab-uniba/socialcde4eclipse/wiki/How-to-import-a-GitHub-project-into-Eclipse)) or download the project and import it manually into Eclipse.

2. Install TomCat on your machine (version 8.5 or higher recommended). See the documentation and download the installer from [TomCat's webpage] (http://tomcat.apache.org/).

3. Set up the server to run from the Eclipse ([see how](https://github.com/OneBusAway/onebusaway/wiki/Setting-Up-a-Tomcat-Server-in-Eclipse)). Set the limits of timeouts to at least 300 seconds to avoid TomCamt to break when downloading the Maven libs.   

4. Add the project into the server

5. Go to the project properties and see if TomCat is listed on the targeted runtime and if it's checked. Then take a look at the Deployment Assembly and verify if the Maven libraries folder is on it. If everything is ok, you will be able to run the project normally on TomCant.

6. Access the app by the home page: localhost:[port]/medmatch/view/home.xhtml


## Running the app on a standalone server

1. Copy the WAR file with the deployed project and paste it inside the folder webapps of TomCats's directory.

2. Start the server and access the app by the home page: localhost:[port]/medmatch/view/home.xhtml

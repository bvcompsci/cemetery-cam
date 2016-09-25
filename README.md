#Camera App Delta#
###Introduction###
The purpose of this app is for taking picture at the cememtary, get gps, and upload to
`sl-cem`. Pictures are uploaded to the server. The server does everything else and link the gps cordinates on google map. If you want to search for anyone, you will get the excact location where the burial is located and a picture that goes with it on google map. The app make it easy because you don't need to know the gps cordinates or worry about the database. 
###Installation###
This app works along with sl-cem. To start developing, set up the server.
Go to this repo and follow the instruction to get the server running.

[https://github.com/jbshep/sl-cem](https://github.com/jbshep/sl-cem)

#####Require dependencies#####
* python 2.7
* kivy for python 2.7
* buildozer ==> to build the app
* python 3rd party library
    * requests

#####Link to dependencies#####
* [Kivy download](http://kivy.org/#download)
* [Python](https://www.python.org)
* Request- pip install requests

Buildozer only works on *lunix base operating system*. Otherwise, please use the kivy App Launcher.
You can download the launcher from Google Play Store.

Clone the repo and start developing. This app only works on android devices.
So you need `buildozer` to create an `apk`. 
```
git clone https://github.com/jbshep/cam-delta
```
####HOW TO USE THE APP####
######Front Page######
The front page of the app. Allows you to search, use either single/multi upload, and select the query results
![alt text](screenshots/4.png?raw=true "Front page")
######Camera######
Allows you to go  to the camera and take picture. The picture is uploaded to the server or --if multi upload is on--append to 'burial-col.json' file.
![alt text](screenshots/5.png?raw=true "Camera page")

######Option to use multi/single upload######
You can use either single or multi upload. If you don't want to use your 4G/3G data, 
the multiupload option allows you to save everyone you have taken picture of.
 Multiupload create a `burial-col.json` file in your main storage.
![alt text](screenshots/1.png?raw=true "Option")
######Upload to server using the burial-col.json file######
The upload option looks for `burial-col.json` file in you main storage.
Upload if the file exists. It removes the file after is uploaded
![alt text](screenshots/3.png?raw=true "Upload")


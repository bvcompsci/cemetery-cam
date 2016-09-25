#!/usr/bin/python2
__version__ = "1.0"
from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.properties import StringProperty, ObjectProperty
from kivy.uix.button import Button

Builder.load_string("""
#:import FrontPage FrontPage
#:import CameraCem CameraCem
#:import Upload BulkUpload
<CamApp>:
    id:cam_app
    get_first_screen:front_screen
    get_second_screen:second_screen
    get_third_screen:third_screen
    FrontScreen:
        name:"front"
        id:front_screen
    SecondScreen:
        name:"camera"
        id:second_screen
    ThirdScreen:
        id:third_screen
        name:'upload'

<FrontScreen>:
    FrontPage:
<SecondScreen>:
    CameraCem:
<ThirdScreen>:
    Upload:
        """)
class FrontScreen(Screen):
    def __init__(self,**kwargs):
        super(Screen, self).__init__(**kwargs)

class SecondScreen(Screen):
    def __init__(self,**kwargs):
        super(SecondScreen,self).__init__(**kwargs)
        
class ThirdScreen(Screen):
    def __init__(self,**kwargs):
        super(ThirdScreen,self).__init__(**kwargs)

class CamApp(ScreenManager):
    def __init__(self,**kwargs):
        super(CamApp,self).__init__(**kwargs)
    def reset_upload(self):
        for child in self.get_third_screen.children:
            child.get_message_label.text = "Upload to server using the json file"
            child.get_progress.value = 0

    def goto_cam(self,person_name):
        upload_type = "Single"
        for child in self.get_first_screen.children:
            if child.get_upload_type.text == "Multi":
                upload_type = "Multi"
        self.text_from_button = person_name.split("--")
        self.person_id = self.text_from_button[0]
        self.person_name = self.text_from_button[1]
        for child in self.get_second_screen.children:
            child.set_info.text = "Take picture at this location?"
            child.person_id = int(self.person_id)
            child.person_name = self.person_name
            child.upload_type = upload_type

class TestApp(App):
    def build(self):
        return CamApp()
    def on_pause(self):
        return True
    def on_resume(self):
        pass

if __name__ == "__main__":
    TestApp().run()

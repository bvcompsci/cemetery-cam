from kivy.app import App
from plyer import camera, gps
from kivy.uix.label import Label
from kivy.uix.boxlayout import BoxLayout
import CemPost    
import JsonEditor
from kivy.lang import Builder

Builder.load_string("""
<CameraCem>:
    label_var:label_ID
    get_person_info:person_info
    set_info:info
    orientation:'vertical'
    Label:
        id:person_info
        text:""
        size_hint_y:0.5
    Label:
        id:info
        text:"Take picture at this location?"
        size_hint_y:1
    Label:
        text:"GPS"
        id:label_ID
        size_hint_y:2
    Button:
        text:"Check info"
        size_hint_y:0.5
        on_press:root.check_info()
    Button:
        text:"Take Picture"
        size_hint_y:1
        on_press:root.go_to_cam(self)
    Button:
        text:'Go Back'
        on_press:app.root.current = 'front'
        size_hint_y:0.5
        """)
class CameraCem(BoxLayout):
    def __init__(self, **kwargs):
        super(CameraCem, self).__init__(**kwargs)
        self.upload_type = "Single"
        self.person_id = 0
        self.person_name = ""
        # Once the gps is configured, start the gps
        gps.configure(on_location=self.on_location) 
        gps.start()
    def on_location(self, **kwargs):
        self.label_var.text = "{lat}:{lon}".format(**kwargs)

    def check_info(self):
        self.get_person_info.text = "First name: " + self.person_name + " " + "ID: " + str(self.person_id)
    def go_to_cam(self, e):
        gps.stop()
        jpg = ".jpg"
        strcheese = "/storage/emulated/0/{}{}".format(self.person_name.replace(" ",""),jpg)
        camera.take_picture(strcheese ,self.done)

    #When done taking a picture, start the gps again
    def done(self, e):
        cam = CemPost.CemPost()
        gps_cordinates = self.label_var.text.split(":")
        gps_lat = gps_cordinates[0]
        gps_lng = gps_cordinates[1]
        self.set_info.text = "Success! Good job boy scout!"
        if self.upload_type == "Single":
            cam.post_to_slcem(self.person_id,e,gps_lng, gps_lat)
        else:
            jsonEditor = JsonEditor.JsonEditor("/storage/emulated/0/burial-col.json")
            person = {'id':self.person_id,'image-burial':e,'lng':gps_lng,'lat':gps_lat}
            person['name'] = self.person_name
            jsonEditor.write(person)
            
        gps.start()

class CameraApp(App):
    def build(self):
        return CameraCem()
    #When using screens, or if this widget has a parent, this 
    #will not work.
    def on_pause(self):
        return True
    def on_resume(self):
        pass
if __name__ == "__main__":
    CameraApp().run()

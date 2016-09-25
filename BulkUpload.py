from kivy.app import App
from kivy.uix.progressbar import ProgressBar
from kivy.uix.label import Label
from kivy.uix.button import Button
from kivy.uix.boxlayout import BoxLayout
from kivy.lang import Builder
import json
import CemPost
import os

# Another way to use kv language without using .kv file
Builder.load_string('''
<Upload>:
    get_message_label:message
    get_upload_btn:upload_btn
    get_cancel_btn:cancel_btn
    get_screenmanager:app
    get_progress:progress
    orientation:'vertical'
    Label:
        size_hint_y:3
        id:message
        text:'Upload to server using the json file'
    BoxLayout:
        orientation:'vertical'
        Label:
            text:"Loading"
        ProgressBar:
            max:1000
            id:progress
    Button:
        size_hint_y:1
        id:upload_btn
        text:"Upload"
        on_press:root.upload()
    Button:
        size_hint_y:1
        id:cancel_btn 
        text:"Cancel"
        on_press:app.root.current = 'front'
''')
class Upload(BoxLayout):
    def __init(self,**kwargs):
        super(Upload,self).__init__(**kwargs)
        self.get_message_label.text = str(filename) + " doesn't exist!"
        if not os.path.exists(filename):
            self.get_message_label.text = str(filename) + " doesn't exist!"
    def upload(self):
        # Location where the burial-col file is stored.
        # Same as the img file
        # Check if the file already exists
            #if not :  Can't bulk upload, return and  nothing really happened
            #else : use it 
        # The file is save in .json format so that it can be converted into a python
        # dictionary object
        # Go through each document in the file and upload to server
        filename = "/storage/emulated/0/burial-col.json"
        if not os.path.exists(filename):
            self.get_message_label.text = str(filename) + " doesn't exist!"
            return
        data = json.loads(open(filename).read())
        cem = CemPost.CemPost()
        people_size = len(data['objects'])
        for person in data['objects']:
            self.get_progress.value = self.get_progress.value + self.get_progress.max / people_size
            cem.post_to_slcem(person['id'],person['image-burial'],\
                    person['lng'], person['lat'])
        os.remove(filename) 
        self.get_message_label.text = "json file is removed"
class MultiUpload(App):
    def build(self):
        return Upload()
if __name__ == "__main__":
    MultiUpload().run()

from kivy.app import App
from kivy.lang import Builder
from kivy.uix.widget import Widget
from kivy.uix.scrollview import ScrollView
from kivy.uix.popup import Popup
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.gridlayout import GridLayout
from functools import partial
import json
import urllib2
import JsonQuery
Builder.load_file('front.kv')

class FrontPage(Widget):
    def __init__(self, **kwargs):
        super(FrontPage,self).__init__(**kwargs)
        self.date="date"
        self.year = "year"
        self.month = "month"
        self.birth_date=""
    # Request burial-summary for the server
    # The find method is use to search for someone in the collection
    # Arguments :
        #Query: The datatype for this is a dictionary. Ex: {'first_name':'test'}
            # fields(optional): {'fields':['one','two']} 
            # If specified, only project the selected fields
                # it's like: select name,year from Table
        #json_data: the dictionary that we will search through. 
    # The result is a dictionary. We then use the dictionary to populate it on the screen
    # The names are clickable and will foward to the 'camera' page if they get clicked.
    def getUsers(self):
        try:
            self.all_names = [] 
            API = "https://cemetery-map.herokuapp.com/api/burial-summary"
            response = urllib2.urlopen(API)
            json_data = json.load(response)
            if(self.date == "date" and self.month == "month"):
                # year has a selected value that is not 'year', 'year' is the default text
                if self.year != "year":
                    self.birth_date = self.year
                else:
                    self.birth_date = ""
            else:
                self.birth_date = self.month + "-" + self.date + "-" + self.year

            fields =  ['id','first_name','last_name','birth_date']
            query = {'fields':fields, 'first_name':self.get_firstname.text, \
                    'last_name':self.get_lastname.text, 'birth_date':self.birth_date}
            query_system = JsonQuery.JsonQuery()
            json_results = query_system.find(query, json_data)
           
            for person in json_results:
                self.all_names.append(str(person["id"]) + "--" + person["first_name"] + "--" + \
                        person["last_name"] + "--" + person["birth_date"])
            self.search_result.adapter.data = []
            self.search_result.adapter.data.extend(self.all_names)
            self.search_result._trigger_reset_populate()
            
            # This here just reset everything back to default if no the query returns no result.
            if len(self.all_names) <= 0:
                self.get_firstname.text=""
                self.get_lastname.text=""
                self.get_year.text = "year"
                self.get_month.text = "month"
                self.get_date.text = "date"
                self.year = "year"
                self.month = "month"
                self.date = "date"
        except urllib2.HTTPError as e:
           print e 

    def pickYear(self):
        layout = GridLayout(cols=2, size_hint=(1,None))
        layout.bind(minimum_height=layout.setter("height"))
        
        for i in range(1890,2015):
            btn = Button(text=str(i),size_hint_y=None,size_hint_x=1)
            layout.add_widget(btn)
        layout.add_widget(Button(text="",size_hint_y=None,size_hint_x=1))
        scroll = ScrollView(size_hint=(1,1),do_scroll_x=False)
        scroll.add_widget(layout)
        popup = Popup(title="Year", content=scroll,size_hint=(None,None),size=(self.width/2,self.height/1.2))
        popup.open()
        for i in layout.children:
            i.bind(on_press=popup.dismiss)
            i.bind(on_press=partial(self.getBirthdate, i.text,self.month,self.date))
    # Popup screens that allow you to pick the year, date, and month.
    def pickDate(self):
        layout = GridLayout(cols=7, size_hint=(1,1))
        
        for i in range(1,32):
            text_raw = str(i)
            if i < 10:
                text_raw = "0" + text_raw 
            btn = Button(text=text_raw,size_hint=(1,1))
            layout.add_widget(btn)
        layout.add_widget(Button(text="",size_hint_y=None,size_hint_x=1))
        popup = Popup(title="Date", content=layout,size_hint=(None,None),size=(self.width/1.4,self.height/1.4))
        popup.open()
        for i in layout.children:
            i.bind(on_press=popup.dismiss)
            i.bind(on_press=partial(self.getBirthdate, self.year,self.month,i.text))

    def pickMonth(self):
        layout = GridLayout(cols=1, size_hint=(1,None))
        layout.bind(minimum_height=layout.setter("height"))
        
        for i in range(1,13):
            text_raw = str(i)
            if i < 10:
                text_raw = "0"+text_raw
            btn = Button(text=text_raw,size_hint_y=None,size_hint_x=1)
            layout.add_widget(btn)
        layout.add_widget(Button(text="",size_hint_y=None,size_hint_x=1))
        scroll = ScrollView(size_hint=(1,1),do_scroll_x=False)
        scroll.add_widget(layout)
        popup = Popup(title="Month", content=scroll,size_hint=(None,None),size=(self.width/2.5,self.height/1.2))
        popup.open()
        for i in layout.children:
            i.bind(on_press=popup.dismiss)
            i.bind(on_press=partial(self.getBirthdate,self.year,i.text,self.date))
     
    def getBirthdate(self,year="",month="",date="",arg=""):
        if year == "":
            year = "year"
        if month == "":
            month = "month"
        if date == "":
            date = "date"
        self.get_year.text = str(year)
        self.get_month.text = str(month)
        self.get_date.text = str(date)
        self.year = year
        self.month = month
        self.date = date
        
class FrontPageApp(App):
    
    def build(self):
        return FrontPage()

if __name__ == "__main__":
    FrontPageApp().run()

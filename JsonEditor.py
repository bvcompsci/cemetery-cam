import os
import json

# Write to a json file in a nice json format
# If the file doesn't exist, create one and use it
# else, use the file
class JsonEditor:
    def __init__(self,filename='burial-collection.json'):
        self.filename = filename
        self.collection_name = 'objects'
        self.collection = {}
        self.people = []
        initial_list = []
        if not os.path.exists(self.filename):
            os.mknod(self.filename)
            self.collection[self.collection_name] = initial_list
            with open(self.filename,'w') as fout:
                json.dump(self.collection, fout,ensure_ascii=False)
        self.get_collection = json.loads(open(self.filename).read())
        self.people = self.get_collection[self.collection_name]

    def write(self,person, unique_id='id'):
        exist = False
        for per in self.people:
            if per[unique_id] == person[unique_id]:
                exist = True
                per.update(person)

        if exist == False:
            self.people.append(person)
        self.get_collection[self.collection_name] = self.people
        with open(self.filename, 'w') as fout:
            json.dump(self.get_collection, fout, indent=4, ensure_ascii=False)    
    def read(self):
        data = json.loads(open(self.filename).read())
        return data
if __name__ == "__main__":
    jsonC = JsonEditor('hh.json')
    a = jsonC.read()
    print a
    person = {"id":"3", "name":"loc23", "age":"22"}
    person1 = {"id":"3", "name":"lo3", "age":"22"}
    person2 = {"id":"4", "name":"loc23", "age":"22"}
    jsonC.write(person)
    jsonC.write(person1)
    jsonC.write(person2)
    a = jsonC.read()
    for person in a['objects']:
        print person

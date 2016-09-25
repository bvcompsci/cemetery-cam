
class JsonQuery():
    #example dict = {'first_name':'f_name', 'year':'0-00-1999-'}
    # return a list of dictionary containing all the results
    # Requires two arguments
        # 1. The query
        # 2. The json data
    # Note that it only return the field(s) that you selected unless
    # a fields is specified. 
    # without fields
        # For example: {'first_name':'','last_name':''} will only return
        # the first_name and last_name
    # with fields
        # For example: {'fields':['first_name'],'first_name':'','last_name':''} will only return
        # only return first_name because fields has only first_name

    # There are no indexing, no optimizing, so it's not super efficient but
    # get the job done.
    def find(self,query_data={}, json_data=[]):
        select_fields = []
        people = json_data
        if query_data.has_key('fields'):
            select_fields = query_data['fields']
            query_data.pop('fields')
        else:
            select_fields = query_data.keys()
        for key, val in query_data.iteritems():
            temp_people = []
            for person in people:
                if val.lower() in person[key].lower(): 
                    person_doc = {}
                    for field in select_fields:
                        person_doc.update({field:person[field]})
                    temp_people.append(person_doc)
            people = temp_people
        return people
# EXAMPLE
if __name__ == "__main__":
    import json
    import urllib2
    API = "https://cemetery-map.herokuapp.com/api/burial-summary"
    response = urllib2.urlopen(API)
    json_data = json.load(response)
    qur = {'fields':['first_name','last_name'],'last_name':'berge',\
            'first_name':'Doris A','birth_date':''}
    query = JsonQuery()
    a  = query.find(qur,json_data)
    print a

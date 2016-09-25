import requests
class CemPost():
    def post_to_slcem(self, id, img, lng, lat):
        files1 = {}
        files1['headstone_img'] = open(img,'rb')
        data1 = {}
        data1={'id':id, 'lng':lng, 'lat':lat}
        #ALWAYS Include the http:// 
        #requests will throw an error, took me a while to figure this out
        #on android.
        url = "https://cemetery-map.herokuapp.com/api/update-burial"
        r = requests.post(url,files=files1, data=data1)
        print r.text

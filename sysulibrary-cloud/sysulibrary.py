#coding:utf-8

from funny.views import app
from funny.config import address




if __name__ == '__main__':
    app.run(host=address['HOST'], port=address['PORT'], debug=True) 

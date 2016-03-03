#coding:utf-8

import requests
from bs4 import BeautifulSoup
import re
from config import BASE_URL
import logging
import string
from keyczar import keyczar
import os

########################################################################
class Model:
    """"""
    _headers = {'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0'}
    _code_of_sub_library = {u'中山大学图书馆':'',#'ZSU50'
                            u'南校区中文流通 ':'NXZLT',
                            u'南校区外文流通':'NXWLT',
                            u'南校区报刊':'NXBK',
                            u'特藏部 ':'NXTC',
                            u'南校区参考咨询':'NXCK',
                            u'北校区流通':'BXLT',
                            u'北校区图书阅览':'BXYL',
                            u'北校区报刊阅览':'BXBK',
                            u'东校区流通':'DXLT',
                            u'东校区阅览':'DXYL',
                            u'东校区专藏室':'DXZC',
                            u'东校区法学馆':'DXFX',
                            u'东校区地库':'DXDK',
                            u'行政管理中心':'DXXZ',
                            u'珠海校区流通':'ZXLT',
                            u'珠海校区阅览':'ZHYL',
                            u'经管分馆阅览':'LNYL',
                            u'经管分馆流通':'LNLT'}
    _code_of_keytype = {u'所有字段':'WRD',
                        u'正题名(精确匹配)':'ETI',
                        u'题名关键词':'WTI',
                        u'著者':'WAU',
                        u'主题词':'WSU',
                        u'出版社':'WPU',
                        u'ISSN':'ISS',
                        u'ISBN':'ISB',
                        u'索书号':'CAL',
                        u'系统号':'SYS',
                        u'条形码':'BAR',
                        u'户标签':'TAG'}    
    
    
    #----------------------------------------------------------------------
    def __init__(self):
        """Constructor"""
        pass
    #----------------------------------------------------------------------
    def dePwd(self, password):
        """AES"""
        path = IMAGE_DIR = os.path.join(os.path.split(os.path.realpath(__file__))[0], './keys')
        crypter = keyczar.Crypter.Read(path)
        return crypter.Decrypt(password)
        
    #----------------------------------------------------------------------
    def login(self, username, password):
        """登陆"""
        #logging.error(password)
        password = self.dePwd(password)
        #logging.error(password)
        jsonstr = {}
        payload = {'func':'login-session','login_source':'bor-info','bor_library':'ZSU50','bor_id':username,'bor_verification':password}
        r = requests.post(BASE_URL, data=payload, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要
        
        flag = re.search(r'type=password', r.text)
        if flag:
            jsonstr = {
                "code":0,
                "msg":u'登陆失败',
                "token":""}     
            return jsonstr
        else:
            flag = re.search(r'[A-Z0-9]{50}', r.text)
            jsonstr = {
                "code":1,
                "msg":u'登陆成功',
                "token":flag.group()} 
            jsonstr.update(self.get_user_info(jsonstr['token']))
            return jsonstr
    #----------------------------------------------------------------------
    def get_user_info(self, token):
        """获取用户个人信息"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-35178?func=bor-info'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要
        flag = re.search(r'file_name=login-session', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询个人信息成功'
            flag = re.search(r'[A-Z0-9]{50}', r.text)
            jsonstr['token'] = flag.group()
            jsonstr.update(self.parse_user_info(r.text))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_user_info(self, text):
        """解析出个人信息"""
        jsonstr = {}
        soup = BeautifulSoup(text)
        
        keys_ch = [u'姓名', u'地址', u'E-mail', u'地址有效期起', u'地址有效期止', u'邮政编码', u'联系电话(1)', u'联系电话(2)', u'联系电话(3)', u'联系电话(4)', u'状态', u'类型', u'条码号', u'注册有效期', u'外借', u'借阅历史列表', u'预约请求', u'现金事务', u'当前过期外借欠款']
        keys_en = ["name", "address", "email", "addr_start", "addr_end", "postalcode", "phone1", "phone2", "phone3", "phone4", "state", "type", "number", "valid_date", "borrow_num", "borrow_history", "order_num", "cash", "debt"]
        for (key_ch, key_en) in zip(keys_ch, keys_en):
            s = soup.find(lambda tag: tag.text.strip() == key_ch and tag.name=='td')
            if not s==None:
                s = s.parent.stripped_strings
                try:
                    s.next()
                    jsonstr[key_en] = s.next()
                except StopIteration:
                    jsonstr[key_en] = "" 
            else:
                jsonstr[key_en] = ''
        return jsonstr

    #----------------------------------------------------------------------
    def get_book_all_info(self, token, doc_number):
        """通过图书的系统号获取其详细信息"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-04321?func=direct&doc_number=' + doc_number + '&format=999&local_base=ZSU01'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询图书信息成功'
            jsonstr.update(self.parse_book_info(r.text))
            jsonstr.update(self.get_book_order_info(token, doc_number))
            return jsonstr

    #----------------------------------------------------------------------
    def get_book_info(self, token, doc_number):
        """通过图书的系统号获取其详细信息"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-04321?func=direct&doc_number=' + doc_number + '&format=999&local_base=ZSU01'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'请求的记录在当前库中不存在。', r.text)
        if flag:
            jsonstr['code'] = 2
            jsonstr['msg'] = u'请求的记录在当前库中不存在。'
            return jsonstr         
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询图书信息成功'
            jsonstr.update(self.parse_book_info(r.text))
            #jsonstr.update(self.get_book_order_info(token, doc_number))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_book_info(self, text):
        """解析出图书信息"""
        jsonstr = {}
        #text = text.replace(r'<span class=text3 id=normalb>', '')
        #text = text.replace('')
        soup = BeautifulSoup(text)
        
        keys_ch = [u'系统号- 图书', u'ISBN', u'作品语种', u'题名', u'出版发行', u'载体形态', u'摘要', u'主题', u'个人著者']
        keys_en = ['doc_number', 'isbn', 'language', 'name', 'publisher', 'structure', 'summary', 'subject', 'author']
        for (key_ch, key_en) in zip(keys_ch, keys_en):
            s = soup.find(lambda tag: tag.text.strip() == key_ch and tag.name=='td')
            if not s==None:
                s = s.parent.stripped_strings
                val = ''
                try:
                    s.next()
                    val = s.next()
                except StopIteration:
                    val = "" 
                try:
                    val += s.next()
                except StopIteration:
                    pass
                jsonstr[key_en] = val
            else:
                jsonstr[key_en] = ''
        jsonstr['year'] = jsonstr['publisher'].split(',')[1].strip()
        #jsonstr['publisher'] = jsonstr['publisher'].split(',')[0]
        isbn = jsonstr['isbn'].split(':')[0].strip()
        jsonstr['img_url'] = 'http://202.112.150.126/index.php?client=libcode&isbn='+isbn+'/cover'
        return jsonstr

    #----------------------------------------------------------------------
    def get_book_order_info(self, token, doc_number):
        """获得图书的预约信息"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/'+token+'-09132?func=item-global&doc_library=ZSU01&doc_number='+doc_number+'&year=&volume=&sub_library='
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询图书预约信息成功'
            jsonstr.update(self.parse_book_order_info(r.text))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_book_order_info(self, text):
        """解析出图书预约信息"""
        jsonstr = {}
        soup = BeautifulSoup(text)
        orders = []
        ss = soup.find_all(lambda tag: tag.text.strip() == u'详细' and tag.name=='a')
        for s in ss:
            url = s.get('href')
            r = requests.get(url, headers=self._headers)
            r.encoding = 'utf8' #这个超级重要
            order = self.parse_book_single_order_info(r.text)
            st = s.parent.find(lambda tag: tag.text.strip() == u'预约' and tag.name=='a')
            if st==None:
                order['can_order'] = False
                order['PICKUP'] = []
            else:
                order['can_order'] = True
                url = st.get('href')
                r = requests.get(url, headers=self._headers)
                r.encoding = 'utf8' #这个超级重要 
                st = BeautifulSoup(r.text)
                st = st.find('select', attrs={'name':'PICKUP'})
                PICKUP = [li for li in st.stripped_strings]
                order['PICKUP'] = PICKUP
            orders.append(order)
        jsonstr['order_info']  = orders
        return jsonstr
    #----------------------------------------------------------------------
    def parse_book_single_order_info(self, text):
        """获取图书在单馆的预约情况"""
        jsonstr = {}
        text = text.replace(r"<br>", ' ')
        soup = BeautifulSoup(text)
        keys_ch = [u'描述:', u'单册状态:', u'应还日期:', u'应还时间:', u'分馆:', u'收藏地:', u'索书号:', u'请求数量:', u'条码号:']
        keys_en = ['description', u'state', 'due_date', 'due_time', 'sub_library', 'place', 'book_num', 'order_num', 'bar_code']
        for (key_ch, key_en) in zip(keys_ch, keys_en):
            s = soup.find(lambda tag: tag.text.strip() == key_ch and tag.name=='td')
            if not s==None:
                s = s.parent.stripped_strings
                try:
                    s.next()
                    jsonstr[key_en] = s.next()
                except StopIteration:
                    jsonstr[key_en] = "" 
            else:
                jsonstr[key_en] = ''
        jsonstr['order_num'] = jsonstr['order_num']=='' and '0' or jsonstr['order_num']
        return jsonstr
    
    #----------------------------------------------------------------------
    def get_my_bor_loan(self, token):
        """查询个人外借"""
        jsonstr = {}
        url='http://202.116.64.108:8991/F/'+token+'-62552?func=bor-loan&adm_library=ZSU50'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询个人外借信息成功'
            jsonstr.update(self.parse_bor_loan(r.text))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_bor_loan(self, text):
        """解析出个人外借信息"""
        jsonstr = {}
        text = text.replace('<br>', '')
        soup = BeautifulSoup(text)
        loans = []
        cnt = 1
        names = ['no', 'author', 'name', 'year', 'due_date', 'sub_library']
        while(True):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='a')
            cnt += 1
            if not s==None:
                loan = {}
                flag = re.search(r'[0-9]{9}', s.parent.parent.find_all('a')[1].get('href'))
                loan['doc_number'] = flag and flag.group() or ''
                loan['select_no'] = s.parent.parent.find(type='checkbox').get('name')
                s = s.parent.parent.stripped_strings
                for name in names:
                    try:
                        loan[name] = s.next()
                    except StopIteration:
                        loan[name] = ''
                loans.append(loan)
            else:
                break
        jsonstr['loans'] = loans
        return jsonstr

    #----------------------------------------------------------------------
    def get_my_bor_history_loan(self, token):
        """查询个人历史借阅"""
        jsonstr = {}
        url='http://202.116.64.108:8991/F/'+token+'-03278?func=bor-history-loan&adm_library=ZSU50'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询个人历史借阅成功'
            jsonstr.update(self.parse_bor_history_loan(r.text))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_bor_history_loan(self, text):
        """解析出个人历史借阅"""
        jsonstr = {}
        text = text.replace('<br>', '')
        soup = BeautifulSoup(text)
        loans = []
        cnt = 1
        names = ['no', 'author', 'name', 'year', 'due_date', 'due_time', 'ret_date', 'ret_time', 'sub_library']
        while(True):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='a')
            cnt += 1
            if not s==None:
                loan = {}
                flag = re.search(r'[0-9]{9}', s.parent.parent.find_all('a')[2].get('href'))
                loan['doc_number'] = flag and flag.group() or ''
                s = s.parent.parent.stripped_strings
                for name in names:
                    try:
                        loan[name] = s.next()
                    except StopIteration:
                        loan[name] = ''
                loans.append(loan)
            else:
                break
        jsonstr['loans'] = loans
        return jsonstr
    
    #----------------------------------------------------------------------
    def get_my_bor_hold(self, token):
        """查询个人预约信息"""
        jsonstr = {}
        url='http://202.116.64.108:8991/F/'+token+'-40772?func=bor-hold&adm_library=ZSU50'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'查询个人预约信息成功'
            jsonstr.update(self.parse_bor_hold(r.text))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_bor_hold(self, text):
        """解析出个人预约信息"""
        jsonstr = {}
        text = text.replace(r'<BR>', '0')
        text = text.replace('<br>', '')
        soup = BeautifulSoup(text)
        loans = []
        cnt = 1
        names = ['no', 'get_place', 'name', 'order_valid', 'meet_date', 'order_state', 'sub_library', 'book_num', 'book_state']
        while(True):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='a')
            cnt += 1
            if not s==None:
                loan = {}
                url = s.get('href')
                r = requests.get(url, headers=self._headers)
                r.encoding = 'utf8' #这个超级重要 
                st = BeautifulSoup(r.text)
                st = st.find('img', attrs={'alt':'Delete Request'})
                if st == None:
                    loan['can_delete'] = False
                else:
                    loan['can_delete'] = True
                    
                hrefs = s.get('href').split('&')[1:]
                for arg in hrefs:
                    t = arg.split('=')
                    loan[t[0]] = t[1]
                del loan['index']
                
                s = s.parent.parent.parent.parent.stripped_strings
                for name in names:
                    try:
                        loan[name] = s.next()
                    except StopIteration:
                        loan[name] = ''
                loans.append(loan)
            else:
                break
        jsonstr['loans'] = loans
        return jsonstr

    #----------------------------------------------------------------------
    def bor_renew_part(self, token, select_nos, cnt):
        """部分续借"""
        cnt = int(cnt)
        jsonstr = {}
        select_nos = '&'.join([li+'=Y' for li in select_nos.split('|')])
        url = 'http://202.116.64.108:8991/F/' + token + '-00542?func=bor-renew-all&renew_selected=Y&adm_library=ZSU50&' + select_nos
        logging.error(url)
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'token未失效'
            jsonstr.update(self.parse_bor_renew_all(r.text, cnt))
            return jsonstr
        
    #----------------------------------------------------------------------
    def bor_renew_all(self, token):
        """全部续借"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-14566?func=bor-renew-all&adm_library=ZSU50'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'token未失效'
            jsonstr.update(self.parse_bor_renew_all(r.text, 1))
            return jsonstr
    #----------------------------------------------------------------------
    def parse_bor_renew_all(self, text, cnt):
        """解析出续借信息"""
        jsonstr = {}
        text = text.replace('<br>', '')
        soup = BeautifulSoup(text)
        jsonstr['msg'] = soup.find('div', class_='title').text.replace(u'管理库 - ', '').replace(':', '').strip()
        loans = []
        names = ['no', 'name', 'state', 'due_date', 'due_time', 'sub_library', 'number', 'fail_reason']
        while(True):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='td')
            cnt += 1
            if not s==None:
                loan = {}
                s = s.parent.stripped_strings
                for name in names:
                    try:
                        loan[name] = s.next()
                    except StopIteration:
                        loan[name] = ''
                loans.append(loan)
            else:
                break
        jsonstr['loans'] = loans
        return jsonstr
    
    #----------------------------------------------------------------------
    def bor_hold_delete(self, token, book_num):
        """取消预约"""
        jsonstr = {}
        url='http://202.116.64.108:8991/F/'+token+'-40772?func=bor-hold&adm_library=ZSU50'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'token仍有效'
            soup = BeautifulSoup(r.text)
            s = soup.find(lambda tag: tag.text.strip() == book_num and tag.name=='td')
            if s == None:
                jsonstr['code'] = -1
                jsonstr['msg'] = u'无法查询该预约'
                return jsonstr 
            url = s.parent.find('a').get('href')
            r = requests.get(url, headers=self._headers)
            r.encoding = 'utf8' #这个超级重要 
            soup = BeautifulSoup(r.text)
            s = soup.find('img', attrs={'alt':'Delete Request'})
            if s == None:
                jsonstr['code'] = -1
                jsonstr['msg'] = u'无法删除该预约，该预约可能已经到达'
                return jsonstr
            url = s.parent.get('href')
            r = requests.get(url, headers=self._headers)
            r.encoding = 'utf8' #这个超级重要  
            flag = re.search(u'预约请求已经被删除。', r.text)
            if flag:
                jsonstr['code'] = 1
                jsonstr['msg'] = u'成功删除预约'
                return jsonstr
            else:
                jsonstr['code'] = 1
                jsonstr['msg'] = u'预约请求已经被删除'
                return jsonstr
    #----------------------------------------------------------------------
    def order_book(self, token, doc_number, sub_library, PICKUP):
        """预约一本图书"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/'+token+'-09132?func=item-global&doc_library=ZSU01&doc_number='+doc_number+'&year=&volume=&sub_library='
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        else:
            soup = BeautifulSoup(r.text)
            s = soup.find(lambda tag: tag.text.strip() == sub_library and tag.name=='td')
            if s == None:
                jsonstr['code'] = -1
                jsonstr['msg'] = u'索书号错误'
                return jsonstr
            else:
                s = s.parent.find(lambda tag: tag.text.strip() == u'预约' and tag.name=='a')
                if s == None:
                    jsonstr['code'] = -1
                    jsonstr['msg'] = u'此书无法预约了' 
                    return jsonstr 
                else:
                    url = s.get('href')
                    r = requests.get(url, headers=self._headers)
                    r.encoding = 'utf8' #这个超级重要
                    jsonstr.update(self.parse_to_order_book(r.text, token, PICKUP))
                    return jsonstr

    #----------------------------------------------------------------------
    def parse_to_order_book(self, text, token, PICKUP):
        """解析信息预约图书"""
        jsonstr = {}
        flag = re.search(PICKUP, text)
        if not flag:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'预约失败，该书可能就在你所希望取书的图书馆处' 
            return jsonstr 
        data = {}
        soup = BeautifulSoup(text)
        s = soup.find(lambda tag: tag.text.strip() == PICKUP and tag.name=='option')
        if not s:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'预约失败，没有这个取书分馆信息'
            return jsonstr 
        PICKUP = s.get('value')
        data['PICKUP'] = PICKUP
        ss = soup.find_all('input', attrs={'type':'hidden'})
        for s in ss:
            data[s.get('name')] = s.get('value')
        s = soup.find('input', attrs={'name':'to'})
        data[s.get('name')] = s.get('value')
        data['x'] = 50
        data['y'] = 7
        url = 'http://202.116.64.108:8991/F/' + token + '-50798'
        r = requests.post(url, data=data, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要
        flag = re.search(u'读者有一个相似复本.', r.text)
        if flag:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'预约失败，读者有一个相似复本'
            return jsonstr
        flag = re.search(u'预约请求已发送，届时请到该地取书', r.text)
        if flag:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'很高兴的告诉你，预约成功了，请耐心等待图书的到来。'
            return jsonstr
        jsonstr['code'] = -1
        jsonstr['msg'] = u'很不幸的告诉你，预约失败了，暂未知原因'
        return jsonstr

    #----------------------------------------------------------------------
    def search(self, token, keytype, keyword, num, op, start_year, end_year, sub_library):
        """图书高级搜索"""
        jsonstr = {}
        okeyword = keyword
        if not keyword:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'至少需要提供检索关键词吧'
            return jsonstr 
        keyword = ' '.join(keyword.split())
        if op.upper() == 'OR':
            keyword = keyword.replace(' ', '+or+')
        else:
            keyword = keyword.replace(' ', '+and+')
        start_year = re.match(r'[0-9]{4}', start_year) and start_year or ''
        end_year = re.match(r'[0-9]{4}', end_year) and end_year or ''
        keytype = self._code_of_keytype.get(keytype, 'WTI')
        num = int(num) 
        sub_library = self._code_of_sub_library.get(sub_library, '')
        url = 'http://202.116.64.108:8991/F/'+ token + '-26316?func=find-b&find_code='+ keytype + '&request='+ keyword + '&local_base=ZSU01&filter_code_1=WLN&filter_request_1=&filter_code_2=WYR&filter_request_2='+ start_year + '&filter_code_3=WYR&filter_request_3='+ end_year + '&filter_code_4=WFM&filter_request_4=&filter_code_5=WSL&filter_request_5='+ sub_library + '&ADJACENT=N'
        #logging.error(url) 
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        text = r.text.replace('<br>', '') 

        flag = re.search(u'检索限制', text)
        if flag:
            soup = BeautifulSoup(text)
            s = soup.find(lambda tag: tag.text.strip() == okeyword and tag.name=='a')
            if s:
                url = s.get('href')
                #logging.error(url)
                r = requests.get(url, headers=self._headers)
                r.encoding = 'utf8' #这个超级重要
                text = r.text.replace('<br>', '')
            else:
                jsonstr['code'] = 2
                jsonstr['msg'] = u'没有找到符合要求的图书记录，建议你放宽检索要求或使用OR运算来再次检索'   
                return jsonstr
        flag = re.search(u'轮排检索', text)
        if flag:
            jsonstr['code'] = 2
            jsonstr['msg'] = u'没有找到符合要求的图书记录，建议你放宽检索要求或使用OR运算来再次检索'
            soup = BeautifulSoup(text)
            ss = soup.find_all('td', attrs={'id':'centered'})
            suggests = []
            for s in ss:
                sug = {}
                sug['hit'] = s.text.strip()
                tmp = s.parent.stripped_strings
                tmp.next()
                sug['word'] = tmp.next()
                suggests.append(sug)
            jsonstr['suggests'] = suggests
            return jsonstr 
        flag = re.search(u'书目检索', text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr         
        flag = re.search(u'1 条记录', text)
        if flag:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'只找到符合条件的一条图书记录，建议你放宽检索要求或使用OR运算来再次检索'
            jsonstr['total'] = 1
            books = []
            book = {}
            book['index'] = 1
            book.update(self.parse_book_info(text))
            books.append(book)
            jsonstr['books'] = books
            return jsonstr
        flag = re.search(u'最大显示记录', text)
        if flag:
            jsonstr['code'] = 1
            jsonstr['msg'] = u'检索成功'
            jsonstr.update(self.parse_search_info(token, text, num))
            return jsonstr
        jsonstr['code'] = -1
        jsonstr['msg'] = u'error'
        return jsonstr
    #----------------------------------------------------------------------
    def parse_search_info(self, token, text, num):
        """解析搜索结果"""
        jsonstr = {}
        soup = BeautifulSoup(text)
        s = soup.find('div', attrs={'id':'hitnum'})
        if not s:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'未知bug'
            return jsonstr
        tmp = s.stripped_strings
        tmp.next()
        tmp = ','.join(tmp.next().split())#split()和split(' ')是不等价的
        tmp = tmp.split(',')
        cnt = int(tmp[1])
        total = int(tmp[5])
        jsonstr['total'] = total
        books = []
        for i in range(num):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='a')
            if not s==None:
                book = {}
                book['index'] = cnt
                sy = s.parent.parent.parent.find('div', class_='itemtitle')
                book['name'] = sy.stripped_strings.next()
                sy = s.parent.parent.parent.find('img', attrs={'style':'border: medium none;'})
                book['img_url'] = sy.get('src')               
                sy = s.parent.parent.parent.find(lambda tag: tag.text.strip() == u'索书号：' and tag.name=='td').parent
                sy = [li for li in sy.stripped_strings]
                book['author'] = sy[1]
                book['book_num'] = sy[-1]                 
                sy = s.parent.parent.parent.find(lambda tag: tag.text.strip() == u'年份：' and tag.name=='td').parent
                sy = [li for li in sy.stripped_strings]
                book['publisher'] = sy[1] 
                book['year'] = sy[-1]
                
                url = s.parent.parent.parent.find('td', attrs={'class':'libs'}).find('a').get('href')
                doc_number = url.split('=')[-1]
                book['doc_number'] = doc_number
                #url = 'http://202.116.64.108:8991/F/%s-04321?func=direct&doc_number=%s&format=999&local_base=ZSU01' % (token, doc_number)
                #r = requests.get(url, headers=self._headers)
                #r.encoding = 'utf8' #这个超级重要 
                #book.update(self.parse_book_info(r.text))
                #book.update(self.get_book_order_info(token, doc_number))

                books.append(book)
                cnt += 1
            else:
                break
        jsonstr['books'] = books
        return jsonstr    
    #----------------------------------------------------------------------
    def parse_search_info0(self, token, text, num):
        """解析搜索结果"""
        jsonstr = {}
        soup = BeautifulSoup(text)
        s = soup.find('div', attrs={'id':'hitnum'})
        if not s:
            jsonstr['code'] = -1
            jsonstr['msg'] = u'未知bug'
            return jsonstr
        tmp = s.stripped_strings
        tmp.next()
        tmp = ','.join(tmp.next().split())#split()和split(' ')是不等价的
        tmp = tmp.split(',')
        cnt = int(tmp[1])
        total = int(tmp[5])
        jsonstr['total'] = total
        books = []
        for i in range(num):
            s = soup.find(lambda tag: tag.text.strip() == str(cnt) and tag.name=='a')
            if not s==None:
                book = {}
                book['index'] = cnt
                sy = s.parent.parent.parent.find(lambda tag: tag.text.strip() == u'年份：' and tag.name=='td').parent
                sy = [li for li in sy.stripped_strings]
                book['year'] = sy[-1]
                url = s.parent.parent.parent.find('td', attrs={'class':'libs'}).find('a').get('href')
                doc_number = url.split('=')[-1]
                url = 'http://202.116.64.108:8991/F/%s-04321?func=direct&doc_number=%s&format=999&local_base=ZSU01' % (token, doc_number)
                
                r = requests.get(url, headers=self._headers)
                r.encoding = 'utf8' #这个超级重要 
                book.update(self.parse_book_info(r.text))
                #book.update(self.get_book_order_info(token, doc_number))
                books.append(book)
                cnt += 1
            else:
                break
        jsonstr['books'] = books
        return jsonstr
    
    #----------------------------------------------------------------------
    def search_jump(self, token, jump, num):
        """跳页"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-18403?func=short-jump&jump=' + jump + '&pag=now'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要   
        flag = re.search(u'书目检索', r.text)
        if flag:
            jsonstr['code'] = 0
            jsonstr['msg'] = u'token已失效'
            return jsonstr 
        flag = re.search(u'超出集合的记录数', r.text)
        if flag:
            jsonstr['code'] = 2
            jsonstr['msg'] = u'超出了集合的记录数'    
            return jsonstr
        jsonstr['code'] = 1
        jsonstr['msg'] = u'跳页成功'  
        num = string.atoi(num)
        jsonstr.update(self.parse_search_info(token, r.text, num))
        return jsonstr
    
    #----------------------------------------------------------------------
    def logout(self, token):
        """退出登陆"""
        jsonstr = {}
        url = 'http://202.116.64.108:8991/F/' + token + '-80202?func=logout'
        r = requests.get(url, headers=self._headers)
        r.encoding = 'utf8' #这个超级重要  
        jsonstr['code'] = 1
        jsonstr['msg'] = u'成功退出登陆'
        return jsonstr
    
    #----------------------------------------------------------------------
    def submit_bug(self, username, bug):
        """"""
        path = os.path.join(os.path.split(os.path.realpath(__file__))[0], './bug.txt')
        with open(path, 'a') as f: 
            f.write('\n'+username.encode('utf8')+": \n")  
            #logging.error(bug)
            f.write(bug.encode('utf8')) 
        
        jsonstr = {}
        jsonstr['code'] = 1
        jsonstr['msg'] = u'感谢你的提交，这个应用因为有你会变得更好！'
        return jsonstr
    
    #----------------------------------------------------------------------
    def print_json(self, jsonstr):
        """打印json"""
        for (key, value) in jsonstr.items():
            print key, ": ", value

if __name__ == '__main__':
    md = Model()
    md.submit_bug('ajj', u'烦烦烦f')
    ##soup = BeautifulSoup(open('index.html'))
    #jsonstr = md.login('', '')
    #token = jsonstr['token']
    #print token
    #doc_number = '000540122'
    ##md.print_json(md.get_book_info(token, doc_number))
    #md.print_json(md.get_my_bor_loan(token))
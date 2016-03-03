#coding:utf-8

from flask import Flask, jsonify, request
import logging 
from models import Model

app = Flask(__name__)
#md = Model()


@app.route('/login/', methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form.get('username', '')
        password = request.form.get('password', '')
        md = Model()
        jsonstr = md.login(username, password)
        return jsonify(jsonstr)
    else:
        return '''
        <!doctype html>
        <title>login</title>
        <h1>login</h1>
        <form action="" method=post>
          <p><input type=text name=username value=>
             <input type=password name=password value=>
             <input type=submit value=Login>
        </form>
        ''' 
@app.route('/get_user_info/', methods=["GET", "POST"])
def get_user_info():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.get_user_info(token)
    return jsonify(jsonstr)

@app.route('/get_book_all_info/', methods=["GET", "POST"])
def get_book_all_info():
    token = request.form.get('token', request.args.get('token', ''))
    doc_number = request.form.get('doc_number', request.args.get('doc_number', ''))
    md = Model()
    jsonstr = md.get_book_all_info(token, doc_number)
    return jsonify(jsonstr)

@app.route('/get_book_info/', methods=["GET", "POST"])
def get_book_info():
    token = request.form.get('token', request.args.get('token', ''))
    doc_number = request.form.get('doc_number', request.args.get('doc_number', ''))
    md = Model()
    jsonstr = md.get_book_info(token, doc_number)
    return jsonify(jsonstr)

@app.route('/get_book_order_info/', methods=["GET", "POST"])
def get_book_order_info():
    token = request.form.get('token', request.args.get('token', ''))
    doc_number = request.form.get('doc_number', request.args.get('doc_number', ''))
    md = Model()
    jsonstr = md.get_book_order_info(token, doc_number)
    return jsonify(jsonstr)

@app.route('/get_my_bor_loan/', methods=["GET", "POST"])
def get_my_bor_loan():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.get_my_bor_loan(token)
    return jsonify(jsonstr)

@app.route('/get_my_bor_history_loan/', methods=["GET", "POST"])
def get_my_bor_history_loan():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.get_my_bor_history_loan(token)
    return jsonify(jsonstr)

@app.route('/get_my_bor_hold/', methods=["GET", "POST"])
def get_my_bor_hold():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.get_my_bor_hold(token)
    return jsonify(jsonstr)

@app.route('/bor_renew_part/', methods=["GET", "POST"])
def bor_renew_part():
    token = request.form.get('token', request.args.get('token', ''))
    select_nos =request.form.get('select_nos', request.args.get('select_nos', ''))
    cnt =request.form.get('cnt', request.args.get('cnt', ''))
    md = Model()
    jsonstr = md.bor_renew_part(token, select_nos, cnt)
    return jsonify(jsonstr)

@app.route('/bor_renew_all/', methods=["GET", "POST"])
def bor_renew_all():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.bor_renew_all(token) 
    return jsonify(jsonstr)

@app.route('/bor_hold_delete/', methods=["GET", "POST"])
def bor_hold_delete():
    token = request.form.get('token', request.args.get('token', ''))
    book_num = request.form.get('book_num', request.args.get('book_num', ''))
    md = Model()
    jsonstr = md.bor_hold_delete(token, book_num)
    return jsonify(jsonstr)

@app.route('/order_book/', methods=["GET", "POST"])
def order_book():
    token = request.form.get('token', request.args.get('token', ''))
    doc_number = request.form.get('doc_number', request.args.get('doc_number', ''))
    sub_library = request.form.get('sub_library', request.args.get('sub_library', ''))
    PICKUP = request.form.get('PICKUP', request.args.get('PICKUP', ''))
    md = Model()
    jsonstr = md.order_book(token, doc_number, sub_library, PICKUP)
    return jsonify(jsonstr)

@app.route('/search/', methods=["GET", "POST"])
def search():
    token = request.form.get('token', request.args.get('token', ''))
    keytype = request.form.get('keytype', request.args.get('keytype', 'WTI'))
    keyword = request.form.get('keyword', request.args.get('keyword', ''))
    num = request.form.get('num', request.args.get('num', '5'))
    op = request.form.get('op', request.args.get('op', 'AND'))
    start_year = request.form.get('start_year', request.args.get('start_year', ''))
    end_year = request.form.get('end_year', request.args.get('end_year', ''))
    sub_library = request.form.get('sub_library', request.args.get('sub_library', 'ZSU50'))
    md = Model()
    jsonstr = md.search(token, keytype, keyword, num, op, start_year, end_year, sub_library)
    return jsonify(jsonstr)

@app.route('/search_jump/', methods=["GET", "POST"])
def search_jump():
    token = request.form.get('token', request.args.get('token', ''))
    jump = request.form.get('jump', request.args.get('jump', ''))
    num = request.form.get('num', request.args.get('num', '5'))
    md = Model()
    jsonstr = md.search_jump(token, jump, num)
    return jsonify(jsonstr)

@app.route('/logout/', methods=["GET", "POST"])
def logout():
    token = request.form.get('token', request.args.get('token', ''))
    md = Model()
    jsonstr = md.logout(token, jump)
    return jsonify(jsonstr)

@app.route('/submit_bug/', methods=['GET', 'POST'])
def submit_bug():
    username = request.form.get('username', request.args.get('username', ''))
    bug = request.form.get('bug', request.args.get('bug', ''))
    md = Model()
    jsonstr = md.submit_bug(username, bug)
    return jsonify(jsonstr) 

@app.route('/')
def main():
    return jsonify({"hello":'呵呵', 'aa':'bb'})

@app.route('/test/<name>', methods=["GET", "POST"])
def test(name):
    logging.error(name+request.args.get('password'))
    return name+"hello"

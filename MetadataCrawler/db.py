import sqlite3
import os
import thread

DBFILE = os.path.dirname(os.path.abspath(__file__)) +'/data.db'
DBLOCK = thread.allocate_lock()

if not os.path.exists(DBFILE):
	conn = sqlite3.connect(DBFILE, check_same_thread=False)
	cur = conn.cursor()
	cur.execute('CREATE TABLE miniapps(docID, nickName, iconUrl, userName, description, path, scoreTfIdf, \
				scoreQuailty, appid PRIMARY KEY, extra_json, appuin, keyword, insertTime);')
	cur.execute('CREATE TABLE searched(wid INTEGER, words PRIMARY KEY);')
	conn.commit()
else:
	conn = sqlite3.connect(DBFILE, check_same_thread=False)
	cur = conn.cursor()

conn.row_factory = sqlite3.Row

def add_new_apps(item, keyword):
	with DBLOCK:
		try:
			SQLins = "INSERT INTO miniapps (docID, nickName, iconUrl, userName, description, path, scoreTfIdf, \
						scoreQuailty, appid, extra_json, appuin, keyword, insertTime) VALUES (?,?,?,?,?,?,?,?,?,?,?,?, CURRENT_TIMESTAMP)"
			cur.execute(SQLins, (item['docID'], item['nickName'], item['iconUrl'], item['userName'], item['description'], item['path'], \
							item['scoreTfIdf'], item['scoreQuailty'], item['appid'], item['extra_json'], item['appuin'], keyword))
			conn.commit()
		except Exception, e:
			if "UNIQUE constraint failed" not in str(e):
				print '[-]', e

def get_apps():
	cursor = conn.execute("SELECT docID, nickName, iconUrl, userName, description, path, scoreTfIdf, \
				scoreQuailty, appid, extra_json, appuin, keyword FROM miniapps")
	return cursor

def get_apps_count():
	cursor = conn.execute("SELECT count(appid) FROM miniapps")
	return cursor.fetchall()[0][0]

def add_searched_word(word):
	with DBLOCK:
		try:
			SQLins = "INSERT INTO searched (wid, words) VALUES ((SELECT IFNULL(MAX(wid), 0) + 1 FROM searched), ?)"
			cur.execute(SQLins, (word,))
			conn.commit()
		except Exception, e:
			print '[-]', e

def get_searched_words():
	cursor = conn.execute("SELECT wid, words FROM searched")
	return cursor

import db
import MiniAppsSearch
import wordsplits


words = {}

with open("defaultWords.txt") as f:
	for i in f:
		i = i.strip()
		words[i] = 1


searched = set()
print db.get_apps_count()
for searched_words in db.get_searched_words():
	searched.add(searched_words['words'])
print '[*] searched', len(searched)


for appmeta in db.get_apps():
	nickName = appmeta['nickName'].lower()

	for i in wordsplits.words_from_miniapp(appmeta):
		if i not in searched:
			if i not in words:
				words[i] = 0
			words[i] += 1

words = sorted(words.items(), key=lambda kv: kv[1], reverse=True)
print '[*] words', len(words)

lastfound = 0
lastcount = 0
def onitems(items):
	global lastfound, lastcount
	print len(items)
	for item in items:
		print lastcount, lastfound, item['appid'], item['nickName'], item['description']
		db.add_new_apps(item)

for word, count in words:
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	print word, count
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	print 'XXXXXXXXXXXXXXXXX'
	oldn = db.get_apps_count()

	MiniAppsSearch._request_by_word(word, onitems)
	db.add_searched_word(word)

	lastcount = db.get_apps_count()
	lastfound = lastcount - oldn
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
	print word, count
	print lastfound
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
	print 'OOOOOOOOOOOOOOOOO'
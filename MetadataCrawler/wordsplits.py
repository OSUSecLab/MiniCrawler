# encoding=utf-8
import jieba

jieba.enable_paddle()

def words_from_miniapp(miniapp):
	nickName = miniapp['nickName'].lower()
	description = miniapp['description'].lower()

	seg_listA = jieba.cut(nickName, cut_all=True)
	seg_listB = jieba.cut(description, cut_all=True)

	words = set(list(nickName) + list(description) + list(seg_listA) + list(seg_listB))

	return words

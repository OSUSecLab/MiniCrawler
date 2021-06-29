## How to run it
MiniCrawler is a scalable and open source WeChat mini-app crawler, it contains two parts:

**Xposed Plugin:** Extracting data (cookie) from WeChat to initialize the mini-app metadata crawler, and injecting downloading commands to WeChat.

**Metadata Crawler:** Using keywords and cookie to send mini-app query messages to WeChat server and retrieve mini-app metadata.

## How to run it

**Crawl Mini-app Metadata** 

1. Install Xposed and WeChat 7.0.19 on your phone.
2. Compile and install XposedPlugin, and enable it on Xposed.
3. Run `adb logcat | grep WechatSearchToken` in terminal.
4. Use Wechat to search for mini-apps with any keyword.
5. In the terminal, you will find string `HTTP_GET_DATA` in format `begid=0&longitude=-...&latitude=...&client_version=...&query=...&...`. Replace the content of `query` with `%%QUERY%%` and put the string in file `httpGetData.txt`
6. Prepare the initial keywords that you want to search for mini-apps to file `defaultWords.txt`(one keyword per line).
7. Run script `main.py`, it will search for mini-app metadata and save them to a created database file `data.db`

**Download Mini-apps** 

1. Install Xposed and WeChat 7.0.20 on your phone.
2. Compile and install XposedPlugin, and enable it on Xposed.
3. Open any mini-app in WeChat.
4. For each mini-app you want download, prepare its `appid`  (such as `wx5054764a3fdfb3b5`) which can be found in `data.db`. 
5. Run command `adb shell am broadcast -a android.intent.myper --es appid "wx5054764a3fdfb3b5"`, and the WeChat will download this mini-app and save its version information and downloading URL to `/sdcard/apps.txt`

## Citation

If you create a research work that uses our work, please cite our paper:

```
@article{zhang2021measurement,
  title={A Measurement Study of Wechat Mini-Apps},
  author={Zhang, Yue and Turkistani, Bayan and Yang, Allen Yuqing and Zuo, Chaoshun and Lin, Zhiqiang},
  journal={Proceedings of the ACM on Measurement and Analysis of Computing Systems},
  volume={5},
  number={2},
  pages={1--25},
  year={2021},
  publisher={ACM New York, NY, USA}
}
```

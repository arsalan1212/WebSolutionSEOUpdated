# WebSolutionSEO Software House

This App take all Videos of a specific youtube Channel and then show it in a recyclerView

## Feature Include
1) Show video published Date and the channel Name

2) Show all Views Count and Comments Count

3) Show all likes and dislikes Count

4) Clicking on a video will play the video in youtube player






### YouTube playlist Json Link

https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLuRcKOVytKXXT8YqZrf7cIazVVkIEbgLf&key=AIzaSyDJqOUx2KV3HvgOYBsBhZ8rDcJ0xxMIsx4&maxResults=50


### Comments on a specific video Json Link

https://www.googleapis.com/youtube/v3/commentThreads?key=AIzaSyDJqOUx2KV3HvgOYBsBhZ8rDcJ0xxMIsx4&textFormat=plainText&part=snippet&videoId=WEr40J1XaoU&maxResults=50


### Specific video Likes, Dislikes and Total Comments on a video Json link

https://www.googleapis.com/youtube/v3/videos?part=statistics&id=WEr40J1XaoU&key=AIzaSyDJqOUx2KV3HvgOYBsBhZ8rDcJ0xxMIsx4


### YouTube Channel all Videos Json Response Link

https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=UCBRBgsoUC893QzkPRsdx8GQ&maxResults=25&key=AIzaSyDJqOUx2KV3HvgOYBsBhZ8rDcJ0xxMIsx4

## For Facebook Authenticuation key hash code <i>procedure</i>

Requirements

1) we need to know path of keytool.exe for me C:\Program Files\Java\jre6\bin\keytool.exe

note : if you dont have it you can download Java SE Development Kit 8u60 form this link
http://www.oracle.com/technetwork/jav...


2) we need to have openssl program for windows you can download it from this link
note : if you have 62bit in your system chose  openssl-0.9.8k_X64.zip
 if you have 84bit in your system chose  openssl-0.9.8k_WIN32.zip
 
https://code.google.com/p/openssl-for...

3) we need to know path of our keystore to use it for getting {Release Key Hash}

4) we need to know alias for app

this ccomment line help us to get list of alias in keystore we will take just alias that we use it for created app


OK! Lets Started to Get [Development Key Hashes] First


Development Key Hashes

keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64


Release Key Hash

keytool -exportcert -alias YOUR_RELEASE_KEY_ALIAS -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64

for Release Key Hash

we need path of our keystore

me i put it inside eclips 

C:\Users\organic\Desktop\eclipse\mykeystore

path keytool C:\Program Files\Java\jre6\bin\keytool.exe
path openssl C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe


we need to know our alias key that we created in keystore

with this commend

keytool -list -keystore C:\Users\organic\Desktop\eclipse\mykeystore

me i created one app with one alias so we will get 1 alias if you created more alias for every app u have to chose one alias for your app u created  

ok see


dont forget to put password that u have created in your keystore


we get 1 alias and date that we created 

aaga, Aug 28, 2015, PrivateKeyEntry,

this is my alias aaga


ok now we will get release hash key with this commend

keytool -exportcert -alias aaga -keystore C:\Users\organic\Desktop\eclipse\mykeystore | C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe sha1 -binary | C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe base64



finally we get release key hash :) enjoy

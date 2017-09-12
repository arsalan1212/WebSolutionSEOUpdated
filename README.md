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

<code>keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64</code>


Release Key Hash

<code>keytool -exportcert -alias YOUR_RELEASE_KEY_ALIAS -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64</code>

<strong> Full path is : <code>
 keytool -exportcert -alias psxTraining -keystore "D:\key\psxTraining.jks" | "c:\openssl\bin\openssl.exe" sha1 -binary | "c:\openssl\bin\openssl.exe" base64
</code></strong>

for Release Key Hash

we need path of our keystore

me i put it inside eclips 

<code>C:\Users\organic\Desktop\eclipse\mykeystore

path keytool C:\Program Files\Java\jre6\bin\keytool.exe
path openssl C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe </code>


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

<code>keytool -exportcert -alias aaga -keystore C:\Users\organic\Desktop\eclipse\mykeystore | C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe sha1 -binary | C:\Users\organic\Desktop\openssl-0.9.8k_X64\bin\openssl.exe base64</code>



finally we get release key hash :) enjoy


## For this i have done it like this
1)<code keytool -exportcert -alias psxTraining -keystore "D:\key\psxTraining.jks" | "c:\openssl\bin\openssl.exe" sha1 -binary | "c:\openssl\bin\openssl.exe" base64></code>

# SHA Certificate for release key
<code>keytool -exportcert -alias psxTraining -keystore D:\key\psxTraining.keystore.jks -list -v</code>
# Key hash for facebook after uploading on PlayStore
### SHA certificate is of App sign on PlayStore i mean playstore SHA certificate you have to enter
<code>
 // GOOGLE PLAY APP SIGNING SHA-1 KEY:- A0:87:65:F8:42:0E:6C:73:EB:46:0B:42:CF:96:8E:4A:54:40:51:71
        byte[] sha1 = {
                (byte)0xA0, (byte)0x87, 0x65, (byte)0xF8, (byte)0x42, 0x0E, 0x6C, (byte)0x73, (byte)0xEB, (byte)0x46, (byte)0x0B, 0x42, (byte) 0xCF, (byte)0x96, (byte) 0x8E, (byte)0x4A, 0x54, 0x40, (byte)0x51, (byte)0x71
        };
       <i> 
        System.out.println("keyhashGooglePlaySignIn:"+ Base64.encodeToString(sha1, Base64.NO_WRAP));
        
        
        Log.d("TAG","KEYHASH: "+Base64.encodeToString(sha1, Base64.NO_WRAP)); </i>
</code>

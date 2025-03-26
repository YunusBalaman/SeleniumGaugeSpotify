Api Test Spec
=====================
     
Api Test Scenario
-------------------
tags:apiTest

 Get Token
* "./jsonFiles/get_access_token.json" json dosyasini string olarak "tokenJson" keyinde tut
* "{{tokenJson}}" json üzerindeki "accessToken" json yolunu oku "String" tipindeki degeri "token" keyinde tut
* "getProjects" api testi için map key olustur
* "https://api.spotify.com" baseUri ı "getProjects" e ekle
* "JSON" contentType degerini "getProjects" e ekle
* "Authorization" "Bearer {{token}}" header degerini "getProjects" e ekle
 "getProjects" için bearer token ekle
Authorization
* "get" requestType ve "/v1/me" i "getProjects" e ekle "String"
* "getProjects" api testi için istek at, log="true"
* "getProjects" api testi statusCode değeri "200" değerine eşit mi

Read Mail
-------------

* Zamanı milisaniye olarak al ve "timeMillis" de tut
* Read Mail "" mail "" password after time "{{timeMillis}}" loopCount "6" if "true"
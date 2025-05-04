Api Test Spec
=====================
     
Api Test Scenario
-------------------
tags:apiTest

 Get Token
* "./jsonFiles/get_access_token.json" json dosyasini string olarak "tokenJson" keyinde tut
* "{{tokenJson}}" json üzerindeki "accessToken" json yolunu oku "String" tipindeki degeri "token" keyinde tut
* "spotifyMe" api testi için map key olustur
* "https://api.spotify.com" baseUri ı "spotifyMe" e ekle
* "JSON" contentType degerini "spotifyMe" e ekle
* "Authorization" "Bearer {{token}}" header degerini "spotifyMe" e ekle
 "getProjects" için bearer token ekle
Authorization
* "get" requestType ve "/v1/me" i "spotifyMe" e ekle "String"
* "spotifyMe" api testi için istek at, log="true"
* "spotifyMe" api testi statusCode değeri "200" değerine eşit mi
* "spotifyMe" json üzerindeki "country" json yolunu oku "String" tipindeki degeri "countrySpotifyMe" keyinde tut
* "spotifyMe" json üzerindeki "display_name" json yolunu oku "String" tipindeki degeri "displayNameSpotifyMe" keyinde tut
* "spotifyMe" json üzerindeki "email" json yolunu oku "String" tipindeki degeri "emailSpotifyMe" keyinde tut
* "spotifyMe" json üzerindeki "id" json yolunu oku "String" tipindeki degeri "idSpotifyMe" keyinde tut
* "spotifyMe" json üzerindeki "product" json yolunu oku "String" tipindeki degeri "productSpotifyMe" keyinde tut
* "spotifyMe" json üzerindeki "type" json yolunu oku "String" tipindeki degeri "typeSpotifyMe" keyinde tut
* "{{idSpotifyMe}}" log olarak ekle
* "{{displayNameSpotifyMe}}" log olarak ekle
* "{{productSpotifyMe}}" log olarak ekle
* "{{emailSpotifyMe}}" log olarak ekle

Read Mail
//-------------

 Zamanı milisaniye olarak al ve "timeMillis" de tut
 Read Mail "" mail "" password after time "{{timeMillis}}" loopCount "6" if "true"
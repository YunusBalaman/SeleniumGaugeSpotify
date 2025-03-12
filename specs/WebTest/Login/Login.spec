Login Spec
=====================
     
Valid Login
----------------
tags:validLogin

* Homepage sayfasında olunduğu kontrol edilir first
* Çerezleri Kabul et
* Ücretsiz Spotify Kayıt ol Bannerı varsa kapat
* HomePage Login butonuna tıklanır
* Login sayfası kontrol edilir
* "" Kullanıcı adi ve "" şifresiyle giriş yapılır
* Şu anki url "https://open.spotify.com/{{spotifyLUrl}}" ile "startWith" durumunu sağlıyor mu "20"
* "2" saniye bekle

Valid Login Devtools
---------------------
tags:validLoginDevtools

* "https://www.google.com/" adresine git
* Set Devtools
* Enable Network
* Listen network api requests
* "https://open.spotify.com/" adresine git
* Homepage sayfasında olunduğu kontrol edilir first
* Çerezleri Kabul et
* Ücretsiz Spotify Kayıt ol Bannerı varsa kapat
* HomePage Login butonuna tıklanır
* Login sayfası kontrol edilir
* "" Kullanıcı adi ve "" şifresiyle giriş yapılır
 Şu anki url "https://open.spotify.com/{{spotifyLUrl}}" ile "startWith" durumunu sağlıyor mu "20"
 Homepage sayfasında olunduğu kontrol edilir
* "20" saniye bekle
* "https://open.spotify.com/get_access_token" apiUrl "get" Network "0"
* Clear listeners and close devtools
* "2" saniye bekle

https://open.spotify.com/get_access_token
https://clienttoken.spotify.com/v1/clienttoken


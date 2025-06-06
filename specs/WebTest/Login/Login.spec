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
* Set devtools listen current request number "devtoolsNumber"
* "" Kullanıcı adi ve "" şifresiyle giriş yapılır
* Login sonrası Anasayfa kontrol edilir
* "1" saniye bekle
* "https://open.spotify.com/get_access_token" apiEndpoint "get" Network "{{devtoolsNumber}}" api response save map "getAccessTokenResponse"
* "getAccessTokenResponse" keyindeki api response body i "./jsonFiles/get_access_token.json" json yoluna yaz
* "https://clienttoken.spotify.com/v1/clienttoken" apiEndpoint "" Network "{{devtoolsNumber}}" api response save map "clienttokenResponse"
* "clienttokenResponse" keyindeki api response body i "./jsonFiles/clienttoken.json" json yoluna yaz
* Clear listeners and close devtools
* "2" saniye bekle

user-widget-link aria-label
https://open.spotify.com/get_access_token
https://clienttoken.spotify.com/v1/clienttoken

# Yazlab1.3-Mobil-Fotograf-Duzenleme

1 - Firebase oluşturuldu.
2 - Firebase Storage aktif edildi.
3 - Firebase Storage rules düzenlendi.
4 - Console.cloud.google aktifleştirildi.
5 - Cloud ile Firebase birleştirildi.
6 - Cloud Shell seçildi. 
7 - Yeni function oluşturuldu. Komutu -> firebase init functions
8 - Firebase üzerinde oluşturulan mevcut depo seçildi.
6 - Yeni bir functions yazmak için typescript seçildi.
8 - Functions içerisine npm kuruldu. Komutu -> npm install -g firebase-tools
9 - cd functions
10 - npm i @google-cloud/storage
11 - npm i sharp
12 - npm i fs-extra
11 - Düzenleme kısmı açıldı ve functions/src/index.ts düzenlendi
11 - Her foto eklendiğinde bizim ayarladığımız alanda yeni bir foto yaratması sağlandı.
11 - Kod Firebase Functiona aktarılıyor. Komutu -> firebase deploy --only functions
11 - Test İçin manuel bir şekilde yeni bir fotoğraf ekleniyor. 
11 - Test OK ! 



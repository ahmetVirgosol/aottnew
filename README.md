SADECE-HB-TC01.spec run edilir  diğer spec dosyası ignore case
LOG VE SCREENSHOT BULUNMAKTADIR | SCREENSHOT -> STEP STEP
Diff case:Sepette baska urun varsa // locator ıle genel sepetı kapsayan alanı seç
cash temızlemeye gerek kalmaz-
Diff case:Ürün sepete eklerken sepete eklenemez moda düşerse / 0 adet kalırsa
-->kontrol şeması ekle ürün count takibi eğer o zaman diliminde 0 ise
sayfa yenile bir diğer ürün için dene program sürdürülebilir olsun
tüm sepet üstünden bir ürün aramak :anyMatch mantığı
\n’lere göre satırlara bölüyor,
en uzun satırı (gerçek ürün adı) seçiyor,
snippet’i bu satırdan üretiyor.
→ Böylece beklenen parça "Lenovo LOQ Intel Core i7 12650HX ..." gibi düzgün ürün adından geliyor.
Tüm div.product_name_2Klj3 a[href] linklerini dolaşıyoruz,
Her birinin başlığında core snippet geçiyor mu diye bakıyoruz,
Herhangi birinde bulursak testi geçiyoruz; hiçbiri eşleşmezse assertion fail.
→ Böylece sepet boş değilse, listede kaç ürün olursa olsun, doğru ürünü bulabiliyoruz; sepeti temizlemeye gerek yok.

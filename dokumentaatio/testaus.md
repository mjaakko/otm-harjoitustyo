## Testaus

Ohjelmaa on testattu sekä yksittäisen luokkien toimintaa testaavilla 
yksikkötesteillä ja luokkakokoelmia testaavilla integraatiotesteillä.
Ohjelman toimintaa kokonaisuudessa on testattu manuaalisilla 
järjestelmätason testeillä erilaisissa tilanteissa.

### Yksikkö- ja integraatiotestaus

#### Sovelluslogiikka

Ohjelman sovelluslogiikkaa eli valuuttamuunnoksia testaa 
integraatiotesti `CurrencyConverterServiceTest` erilaisissa tapauksissa 
(valuuttakursseja ei saatavilla jne.). Integaatiotesti käyttää oikean 
internetistä tulevan datalähteen sijaan 
luokkaa `MockExchangeRateProvider`, joka palauttaa aina sille 
konstruktorissa annetut tiedot. Integraatiotesti ei tallenna tietoja 
tietokantaan vaan käyttää luokkaa `MockExchangeRateDao`, joka tallentaa 
tiedot keskusmuistiin.

Toista sovelluslogiikkaa, eli historiallisia valuuttakursseja, testaa 
integraatiotesti `HistoricalExchangeRateServiceTest`. Tämä testi ei ole 
kovinkaan kattava, mutta testaa kuitenkin, että dataa on vain pyydetyltä 
aikaväliltä.

#### DAO-luokat

`SQLExchangeRateDao` ja `SQLFavoriteCurrencyDao` luokat on testattu 
luomalla tilapäinen tiedosto.

#### Muita testetjä

Testit `ECBExchangeRateProviderTest` ja 
`ECBHistoricalExchangeRateProviderTest` testaavat, että valuuttakurssien 
XML-muotoisen datan parsiminen toimii oikein. Dataa ei ladata verkosta 
vaan se on testiluokan vakiomuuttujasas.

#### Testikattavuus

Käyttöliittymää lukuunottamatta ohjelman testauksen rivikattavuus on 80% 
ja haarautumakattavuus 73%.
Testeissä on joitain puutteita, esimerkiksi tilanteista, joissa 
käyttäjällä ei ole kirjoitusoikeutta tietokantoihin tai data on 
ehdottoman väärää (esim. valuuttakurssi negatiivinen).
Testikattavuutta osaltaan vähentää myös se, että osa metodeista lataa 
tietoja verkosta ja Javan `URLConnection` luokalle on vaikea mockata 
dataa. Testejä ei myöskään voi toteuttaa oikealla datalla, sillä ne 
eivät toimisi esimerkiksi tilanteessa, jossa verkkoyhteyttä ei ole tai 
kohdepalvelin 
on alhaalla.

### Järjestelmätestaus

Järjestelmätestaus on suoritettu manuaalisesti.

Ohjelma on avattu käyttöohjeen mukaisesti niin, että 
suoritushakemistossa on kirjoitusoikeudet.

Ohjelmaa on testattu erilaisissa tilanteissa (esim. ei verkkoyhteyttä -> 
virhedialogit näkyvät oikein) ja kaikki vaatimusmäärittelyn 
toiminnallisuudet on käyty läpi.

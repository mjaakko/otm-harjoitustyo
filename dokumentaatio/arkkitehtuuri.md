# Arkkitehtuuri

## Rakenne

Ohjelma on jaettu kolmella tasolla oleviin pakkauksiin.

1. `valuuttalaskuri.ui` sisältää käyttöliittymään liittyvät luokat.

2. `valuuttalaskuri.common` sisältää palvelut, joiden kautta 
käyttöliittymästä tehdään valuuttamuunnoksia.

3. 
* `valuuttalaskuri.db` sisältää luokat tietokantojen käyttöön.
* `valuuttalaskuri.exchangerateprovider` sisältää luokat, jotka 
tarjoavat valuuttakursseja käytettäväksi.

## Käyttöliittymä

Käyttöliittymässä on kolme näkymää
* normaali valuuttalaskuri
* dialogi valuuttakurssien päivitykseen
* historialliset valuuttakurssit

Normaali valuuttalaskuri on koko ajan näkyvillä ja muut käyttöliittymän 
osat ovat omissa Stageissaan, jotka näkyvät sen päällä.

Käyttöliittymä on pääsääntöisesti eriytetty sovelluslogiikasta, eli 
valuuttakurssien lataaminen ja tallennus sekä itse valuuttamuunnokset 
tehdään olion CurrencyConverterService kautta.

## Sovelluslogiikka

Valuuttakursseja käytetään CurrencyConverterServicen kautta. 
CurrencyConverterService lataa ja tallentaa valuuttakurssit ja antaa 
käytettäväksi CurrencyConverterin, jolla voi tehdä valuuttamuunnokset.

![Luokkakaavio](/dokumentaatio/arkkitehtuuri.png)

![Sekvenssikaavio](/dokumentaatio/sekvenssikaavio.png)

## Tietojen tallennus

Valuuttakurssit ja käyttäjän suosikkivaluutat tallennetaan 
SQLite-tietokantoihin. Sovelluslogiikassa tietokantoja käytetään 
DAO-rajapintojen kautta.

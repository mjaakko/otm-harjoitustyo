# Käyttöohje
## Suorittaminen
`java -jar Valuuttalaskuri.jar`

Ohjelma olettaa, että hakemistossa on kirjoitusoikeudet tietokantoja varten.

Ohjelman voi myös suorittaa ilman käyttöliittymää pelkkien 
valuuttakurssien päivittämiseen komennolla
`java -jar Valuuttalaskuri.jar --update-exchange-rates`. Tämä komento on 
tarkoitettu erityisesti automaattisiin päivityksiin jonkin ajastimen 
(kuten cron) avulla.

## Valuuttamuunnokset

![Valuuttamuunnos](/dokumentaatio/valuuttamuunnos.png)

Ylemmästä dropdown-listasta valitaan valuutta, josta muunnetaan ja alemmasta listasta valuutta, johon muunnetaan.
Kenttään kirjoitetaan määrä, joka halutaan muuttaa.

## Valuuttakurssien päivittäminen

![Päivitys](/dokumentaatio/paivitys.png)

Valikkopalkista valitaan "Asetukset" -> "Päivitä valuuttakurssit", jolloin valuuttakurssit päivittävä dialogi aukeaa.

## Historialliset valuuttakurssit

![Historia](/dokumentaatio/historia.png)

Painamalla "Historia"-nappia aukeaa uusi ikkuna, joka näyttää kaavion valittujen valuuttujen menneistä kursseista. Ikkunasta on mahdollista valita tarkasteltava aikaväli.

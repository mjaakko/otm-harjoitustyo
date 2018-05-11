# Valuuttalaskuri
## Sovelluksen tarkoitus
Sovelluksella on mahdollista laskea valuuttakursseja (esim. 3 GEL = 1 
EUR). Sovelluksella voi myös katsoa vanhoja valuuttakursseja tietyltä 
aikaväliltä.

## Käyttäjät
Sovelluksella on vain yksi käyttäjä.

## Perusversion toiminnallisuus
* Laskuri valuuttakursseille
  * Valuutat dropdown-listassa
* Käyttäjä voi lisätä valuutan suosikkeihin, jolloin se näytetään 
listassa ensimmäisenä
* Kaavio, joka näyttää vanhoja valuuttakursseja käyttäjän valitsemalta 
aikaväliltä (1kk, 3kk, 6kk tai 1v)
* Käyttäjä voi ladata sovelluksella päivitetyt valuuttakurssit 
internetistä
* Mahdollisuus suorittaa sovellus ilman käyttöliittymää valuuttakurssien  
päivitykseen 
  * -> Automaattisesti päivittyvät valuuttakurssit esim. systemd:n tai 
cronin avulla
  * Ohjelma suoritetaan ilman käyttöliittymää, jos sille annetaan 
argumentti `--update-exchange-rates`

### Jatkokehitysideoita
* ~~Mahdollisuus valita mistä lähteestä valuuttakurssit päivitetään~~
* ~~Käyttäjän maan ja valuutan tunnistaminen IP-osoitteen perusteella~~


# Valuuttalaskuri

Sovellus kurssille Ohjelmistotekniikan menetelmät. Valuuttalaskurilla voi laskea valuutan arvon toisessa valuutassa sekä katsoa historiallisia valuuttakursseja.

## Dokumentaatio
* [vaatimusmäärittely](dokumentaatio/vaatimusm%C3%A4%C3%A4rittely.md)
* [tuntiaikakirjanpito](/dokumentaatio/tuntikirjanpito.md)
* [arkkitehtuuri](/dokumentaatio/arkkitehtuuri.md)
* [käyttöohje](/dokumentaatio/kayttoohje.md)
* [testausdokumentti](/dokumentaatio/testaus.md)

## Releaset

* [Viikko 5](https://github.com/mjaakko/otm-harjoitustyo/releases/tag/viikko5)
* [Viikko 6](https://github.com/mjaakko/otm-harjoitustyo/releases/tag/viikko6)
* [Loppupalautus](https://github.com/mjaakko/otm-harjoitustyo/releases/tag/loppupalautus)

## Komentorivitoiminnot
### Testit
Testien suoritus `mvn test`

Testikattavuusraportti `mvn test jacoco:report`
### Jarin generointi
`mvn package`

### Checkstyle
`mvn jxr:jxr checkstyle:checkstyle`

### Javadoc
`mvn javadoc:javadoc`

# Valuuttalaskuri

Sovellus kurssille Ohjelmistotekniikan menetelmät. Valuuttalaskurilla voi laskea valuutan arvon toisessa valuutassa sekä katsoa historiallisia valuuttakursseja.

## Dokumentaatio
* [vaatimusmäärittely](https://github.com/mjaakko/otm-harjoitustyo/blob/master/dokumentaatio/vaatimusm%C3%A4%C3%A4rittely.md)
* [tuntiaikakirjanpito](https://github.com/mjaakko/otm-harjoitustyo/blob/master/dokumentaatio/tuntikirjanpito.md)
* [arkkitehtuuri](https://github.com/mjaakko/otm-harjoitustyo/blob/master/dokumentaatio/arkkitehtuuri.md)

## Komentorivitoiminnot
### Testit
Testien suoritus `mvn test`

Testikattavuusraportti `mvn test jacoco:report`
### Jarin generointi
`mvn package`

### Checkstyle
`mvn jxr:jxr checkstyle:checkstyle`

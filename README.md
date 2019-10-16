# BoaMS
Code repository for the Boa MS android application of the AI project

##Einleitung
Die Digitalisierung schreitet voran. Nicht nur im privaten Umfeld, sondern auch im Unternehmen werden Smartphones, auch als Firmenhandys, immer wichtiger. 
Oftmals wird ein Großteil der Kommunikation mit ihnen geregelt. Daher gibt es viele hilfreiche Apps und Programme auf dem Markt, die den Arbeitsalltag erleichtern können.
Doch weiß man eigentlich, wie sicher diese Programme sind und wer die Daten sowie die geteilten Informationen noch mitliest und speichert?
Ein prominentes Beispiel für diese Apps ist die Anwendung „Teams“ von Microsoft. 
Sie bietet eine Vielzahl an Funktionalitäten, ermöglicht durch ihre Natur als Bestandteil des Office 365 Pakets eine Integration in beinahe jede Bürosystemlandschaft und erlaubt sogar
das Einbeziehen von Drittanbieter-Software. Aber zu welchem Preis? Natürlich kostet es
Geld diese Software nutzen zu können, aber es kostet auch die Zustimmung, dass Microsoft
die Daten speichern darf. Aus unternehmerischer Sicht ist das sicher nicht wünschenswert.
Wäre es nicht viel praktischer und sicher, wenn man seine Dokumente und
Nachrichtenverläufe auf seinem eigenen Server verwalten und speichern sowie die Software
nach seinen eigenen Vorstellungen gestalten und weiterentwickeln könnte?
Auf Basis dieser Motivation wurde die Thematik für dieses Projekt ausgewählt. Auf den
nachfolgenden Seiten werden wir Schritt für Schritt die nötigen Schritte dokumentieren, die
zur Entwicklung und Inbetriebnahme einer solchen firmeneigenen App mit Kommunikation
über das eigene Active-Directory nötig sind.
Diese Anwendung nennen wir ***„Boa-MS“***, kurz für __B__usiness __o__riented __a__pplication –
__m__e__s__saging.


## Konzipierung
von uns angedachten Komponenten aufgelistet. Aus den grundlegenden Anforderungen einer Messaging-Anwendung ergeben sich folgende Basisfunktionen:
- Erstellen von Chats mit einem oder mehreren Nutzern
- Senden und Empfangen von Nachrichten
- Benachrichtigungen bei neu eingegangenen Nachrichten
- Verfügbarkeit der Daten auch ohne Internet-Verbindung

Aus der Unternehmensansicht ergeben sich folgende erweiterte Anforderungen:
- Anmeldung mit einem gültigen Active Directory-Zugang1
- Gesicherte Kommunikation zwischen Server und Client
- Sichere Datenhaltung auf dem Server
- Gewährleistung der Vertraulichkeit der Informationen auf dem Server (d.h. begrenzter Zugang zu den Funktionen der API) sowie eine Ende-zu-Ende Verschlüsselung der Nachrichten

Zusätzlich wurden folgende Funktionen erdacht:
- FTP-File Server als Dateiablage zum Firmennetzwerk, hier sollen Projektdokumentehoch- und runtergeladen werden
- Synchronisation der Active Directory Benutzer als eine Art „Kontaktverzeichnis“

Anmerkung: **Hier wurde sich bewusst für ein Active-Directory (AD) für das Identitätsmanagement entschieden.
Die große Mehrheit aller Unternehmen nutzt den AD Dienst. So kann auf doppelte Datenhaltung verzichtet werden und die App problemlos in diese Betriebe integriert werden.**

Inspirationen zum User-Interface:
- Auswahl von verschiedenen Farbschemata zur Individualisierung der App
- Festlegen von Profilbildern, die in Chats angezeigt werden 

Aus den oben genannten Anforderungen an die Boa MS Anwendung leiten sich folgende
Komponenten ab:
- Active Directory(-Anbindung)
- File-Server
- Datenbank
- API
- Mobile Anwendung


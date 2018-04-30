# Svalker
Svalker is an augmented reality application for Airsoft Role Playing Games(ARPG) inspired by S.T.A.L.K.E.R universe.

It works in a pair with special hardware which converts radio signals to audio signals. During a game, different radio beacons are put in place around a game area, and this determines positions of Anomalies by game conventions.

The application reads audio signal from 3.5 audio jack and analyses it using Fast Fourier Transform(FFT). Different audio frequencies mean different game areas, for example, a location with high radiation.

Data bunching and proceeding implemented mostly with RxJava. Most of the logic runs in background threads binned to the Foreground Service as the detector has to work with a switched off screen. Game items, such as first-aid kits or anti-rad kits consumes as QR codes. For this purpose QR code scanner is build in the application. For generating QR codes, there is the separate application in the repo(CodeGenerator app).

![](static/svalker.jpg)

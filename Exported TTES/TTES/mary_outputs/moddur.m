

clear all;
close all;


y=pvoc(d,0.5,1024);
sound(y,16000);
wavwrite(y,16000,'slow_version1.wav');



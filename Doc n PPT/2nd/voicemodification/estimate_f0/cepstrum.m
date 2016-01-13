[x,fs]=wavread('Theygotoshop.wav');
ms1=fs/1000;                 
ms20=fs/50;                  


t=(0:length(x)-1)/fs;      
 subplot(3,1,1);
 plot(t,x);
 legend('Waveform');
 xlabel('Time (s)');
 ylabel('Amplitude');
Y=fft(x.*hamming(length(x)));



hz5000=5000*length(Y)/fs;
f=(0:hz5000)*fs/length(Y);
 subplot(3,1,2);
 plot(f,20*log10(abs(Y(1:length(f)))+eps));
 legend('Spectrum');
 xlabel('Frequency (Hz)');
 ylabel('Magnitude (dB)');



C=fft(log(abs(Y)+eps));


pms1=round(ms1);
pms20=round(ms20);
q=(pms1:pms20)/fs;
 subplot(3,1,3);
 plot(q,abs(C(pms1:pms20)));
 legend('Cepstrum');
 xlabel('Quefrency (s)');
 ylabel('Amplitude');


[c,fx]=max(abs(C(ms1:ms20)));
fprintf('Fx=%gHz\n',fs/(ms1+fx-1));
 